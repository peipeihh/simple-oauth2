/*
 * Copyright 2018 peipeihh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * limitations under the License.
 */
package com.pphh.oauth.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.pphh.oauth.constant.SecurityActionType;
import com.pphh.oauth.core.response.MessageType;
import com.pphh.oauth.dao.SecurityActionRepository;
import com.pphh.oauth.dao.UserRepository;
import com.pphh.oauth.exception.BaseException;
import com.pphh.oauth.po.UserEntity;
import com.pphh.oauth.po.UserSecurityActionEntity;
import com.pphh.oauth.service.LdapService;
import com.pphh.oauth.service.MailService;
import com.pphh.oauth.service.MetricService;
import com.pphh.oauth.service.UserService;
import com.pphh.oauth.utils.ConvertUtil;
import com.pphh.oauth.utils.EnvProperty;
import com.pphh.oauth.utils.Md5Tool;
import com.pphh.oauth.vo.PageVO;
import com.pphh.oauth.vo.UserVO;
import edu.vt.middleware.password.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.json.JsonParser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Please add description here.
 *
 * @author huangyinhuang
 * @date 7/5/2018
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private SecurityActionRepository securityActionRepo;
    @Autowired
    private MailService mailService;
    @Autowired
    private LdapService ldapService;
    @Autowired
    private EnvProperty envProperty;
    @Autowired
    private MetricService metricService;

    private PasswordGenerator pwdGenerator;
    private List<CharacterRule> pwdRules;

    public UserServiceImpl() {
        pwdGenerator = new PasswordGenerator();
        pwdRules = new ArrayList<CharacterRule>();
        pwdRules.add(new DigitCharacterRule(1));
        pwdRules.add(new NonAlphanumericCharacterRule(1));
        pwdRules.add(new UppercaseCharacterRule(1));
        pwdRules.add(new LowercaseCharacterRule(1));
    }

    /**
     * 使用用户邮箱进行注册，生成随机密码和校验码，最后发送邮件告知用户信息
     *
     * @param email 用户邮箱地址
     * @return 返回注册结果
     */
    @Override
    public Boolean register(String email) {
        if (!isValidEmailAddress(email)) {
            throw BaseException.newException(MessageType.ERROR, "输入的邮箱为无效格式：%s，请检查后重新输入。", email);
        }

        UserEntity existingUser = userRepo.findOneByEmail(email);
        if (existingUser != null) {
            throw BaseException.newException(MessageType.ERROR, "对不起，该邮箱%s已经注册，请直接登录或者尝试找回密码", email);
        }

        // 生成用户名的随机密码、校验码，其中校验码一旦生成则跟随用户名，不再变化
        // 校验码的作用：生成md5码算法为：md5(密码+校验码)，此预防md5字典对用户密码的攻击
        String password = getRandomPassword();
        String checkCode = getCheckingCode();

        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(email);
        userEntity.setName(email);
        userEntity.setCheckcode(checkCode);
        userEntity.setRoles("local");
        userEntity.setLastVisitAt(new Date());

        String md5code = Md5Tool.getStringMd5(password + checkCode);
        userEntity.setPassword(md5code);
        userRepo.save(userEntity);

        String from = "peipeihh@qq.com";
        String subject = "OAuth2管理系统用户注册";
        StringBuilder content = new StringBuilder();
        content.append("欢迎您注册OAuth2管理系统，您的登录信息为：");
        content.append("\n用户名(邮箱)：" + email);
        content.append("\n密码(8位)：" + password);
        content.append("\n请点击链接登录： http://localhost/#/login");

        return mailService.sendMail(from, email, subject, content.toString());
    }

    /**
     * 更新密码，发送邮箱告知用户信息
     *
     * @param email 用户注册时的邮箱
     * @return 返回更新密码结果，是否成功
     */
    @Override
    public Boolean refreshPassword(String email) {
        UserEntity currentUser = userRepo.findOneByEmail(email);
        if (currentUser == null) {
            throw BaseException.newException(MessageType.ERROR, "对不起，该邮箱%s不存在，请确定邮箱正确。", email);
        }

        String password = getRandomPassword();
        String checkCode = currentUser.getCheckcode();
        String md5code = Md5Tool.getStringMd5(password + checkCode);
        currentUser.setPassword(md5code);
        userRepo.save(currentUser);

        String from = "peipeihh@qq.com";
        String subject = "OAuth2管理系统用户密码更新";
        StringBuilder content = new StringBuilder();
        content.append("您好，您的登录信息更新为：");
        content.append("\n用户名(邮箱)：" + email);
        content.append("\n密码(8位)：" + password);
        content.append("\n请点击链接登录： http://localhost/#/login");

        return mailService.sendMail(from, email, subject, content.toString());
    }

    /**
     * 设置登录用户名
     *
     * @param email    用户注册时的邮箱
     * @param userName 用户名
     * @return 返回用户名设置结果，是否成功
     */
    @Override
    public String setUserName(String email, String userName) {
        String jwtToken = null;

        if (userName.isEmpty()) {
            throw BaseException.newException(MessageType.ERROR, "用户名不能为空，请重新输入。");
        }

        UserEntity existingUser = userRepo.findOneByName(userName);
        if (existingUser != null) {
            throw BaseException.newException(MessageType.ERROR, "对不起，用户名%s已经被注册，请选择其它用户名。", userName);
        }

        UserEntity currentUser = userRepo.findOneByEmail(email);
        if (currentUser == null) {
            throw BaseException.newException(MessageType.ERROR, "对不起，邮箱%s不存在，请确定邮箱正确。", email);
        }

        currentUser.setName(userName);
        userRepo.save(currentUser);

        jwtToken = generateJwtToken(userName, email);
        return jwtToken;
    }

    /**
     * 设置密码
     *
     * @param email       用户注册时的邮箱
     * @param newPassword 用户新密码
     * @return 返回更新密码结果，是否成功
     */
    public Boolean setPassword(String email, String newPassword) {
        UserEntity currentUser = userRepo.findOneByEmail(email);
        if (currentUser == null) {
            throw BaseException.newException(MessageType.ERROR, "对不起，该邮箱%s不存在，请确定邮箱正确。", email);
        }

        String checkCode = currentUser.getCheckcode();
        String md5code = Md5Tool.getStringMd5(newPassword + checkCode);
        currentUser.setPassword(md5code);
        userRepo.save(currentUser);

        return Boolean.TRUE;
    }

    /**
     * 设置密码
     *
     * @param token 用户更改密码请求的Token
     * @return 返回更新密码结果，是否成功
     */
    @Override
    public Boolean setPasswordByToken(String token) {
        if (token == null) {
            throw BaseException.newException(MessageType.ERROR, "用户请求有错，请尝试重新执行。");
        }

        UserEntity userInEdit = null;
        String securityOnceActionFlag = null;

        Map<String, Object> loginInfo = parseToken(token);
        String usermail = loginInfo.get("usermail").toString();
        String originalPwd = loginInfo.get("originalPwd").toString();
        String newpwd = loginInfo.get("newpwd").toString();
        String timestamp = loginInfo.get("timestamp").toString();

        // 检查新密码是否为空字符串，若不为空可以继续下一步
        if (newpwd.isEmpty()) {
            throw BaseException.newException(MessageType.ERROR, "新密码不能为空，请重新输入。");
        }

        // 检查用户安全操作，确保用户登录所使用的Token不被重复使用
        // 如果existingSecurityAction不为空，说明该Token已经被使用过，告知用户其已失效
        // 若为空，则说明该Token没有被使用过，可以继续下一步
        if (!isSecurityActionValid(timestamp)) {
            throw BaseException.newException(MessageType.ERROR, "用户登录所使用的Token已失效，请尝试重新执行。");
        } else {
            securityOnceActionFlag = timestamp;
        }

        // 获取待更新密码的用户账号
        userInEdit = userRepo.findOneByEmail(usermail);
        if (userInEdit == null) {
            throw BaseException.newException(MessageType.ERROR, "对不起，该邮箱%s不存在，请确定邮箱正确。", usermail);
        }

        // 检查当前用户输入的原先密码是否正确
        UserEntity user = checkUserLoginByEmail(usermail, originalPwd);
        if (user == null) {
            throw BaseException.newException(MessageType.ERROR, "对不起，用户的原先密码输入错误，请重新尝试。");
        }

        // 执行用户密码更改，设置新密码
        String checkCode = userInEdit.getCheckcode();
        String md5code = Md5Tool.getStringMd5(newpwd + checkCode);
        userInEdit.setPassword(md5code);
        userRepo.save(userInEdit);

        Long userId = null;
        String userName = null;
        if (userInEdit != null) {
            String userMail = userInEdit.getEmail();
            userName = (userInEdit.getName() != null) ? userInEdit.getName() : userMail;
            userId = userInEdit.getId();
        }

        // 记录本次用户安全操作，将标记Token的一次性使用Flag记录到数据库，用于检查Token的失效性
        if (securityOnceActionFlag != null) {
            recordSecurityAction(SecurityActionType.CHANGE_PASSWORD, securityOnceActionFlag, userId, userName);
        }

        return Boolean.TRUE;
    }

    /**
     * 对前端输入的Token进行解码
     * <p>
     * 编码算法: token.reverse + token.charAt(3) + token
     * 解码算法：token.substring((token.length() + 1) / 2, token.length())
     *
     * @param token 登录token
     * @return
     */
    private Map parseToken(String token) {
        int length = token.length();
        token = token.substring((length + 1) / 2, length);
        byte[] bytes = Base64.getDecoder().decode(token);
        String loginInfo = null;
        try {
            loginInfo = new String(bytes, "utf-8");
        } catch (UnsupportedEncodingException e) {
            log.info("用户登录信息格式错误", e);
        }
        JsonParser jsonParser = new JacksonJsonParser();
        return jsonParser.parseMap(loginInfo);
    }

    /**
     * 记录本次用户安全操作，将标记Token的一次性使用Flag记录到数据库，用于检查Token的失效性
     * 一旦Token标记记录到数据库，表明该Token已经被使用过，不能再用于下次请求
     * 所有安全操作之前，需要检查Token的安全标记 - securityOnceActionFlag
     *
     * @param actionType             安全操作类型
     * @param securityOnceActionFlag 安全标记，为某一次安全操作的唯一标记值
     * @param userId                 操作用户ID
     * @param userName               操作用户名
     */
    private void recordSecurityAction(SecurityActionType actionType, String securityOnceActionFlag,
                                      Long userId, String userName) {
        UserSecurityActionEntity securityAction = new UserSecurityActionEntity();
        securityAction.setType(actionType);
        securityAction.setOnceFlag(securityOnceActionFlag);
        securityAction.setUserId(userId);
        securityAction.setUserName(userName);
        securityActionRepo.save(securityAction);
    }

    /**
     * 检查Token的安全标记 - securityOnceActionFlag是否被使用过
     *
     * @param securityOnceActionFlag 安全标记，为某一次安全操作的唯一标记值
     * @return 若没有被使用过，返回True，否则返回False（说明该安全操作已经操作过一次，不再安全）
     */
    private Boolean isSecurityActionValid(String securityOnceActionFlag) {
        UserSecurityActionEntity existingSecurityAction = securityActionRepo.findAction(securityOnceActionFlag);
        return existingSecurityAction == null;
    }

    /**
     * 检查用户登录token是否合法，若合法，则颁发指定有效期的JWT token，用于用户发送后续请求
     *
     * @param token 登录token
     * @return 返回检查结果，如果合法，则颁发指定有效期的Json Web Token，否则返回Null
     */
    @Override
    public String loginUserByToken(String token) {
        if (token == null) {
            throw BaseException.newException(MessageType.ERROR, "用户登录token为空，请尝试重新登录。");
        }

        UserEntity loginUser = null;
        String securityOnceActionFlag = null;

        Map<String, Object> loginInfo = parseToken(token);
        String username = loginInfo.get("username").toString();
        String userpwd = loginInfo.get("userpwd").toString();
        String timestamp = loginInfo.get("timestamp").toString();

        // 检查用户安全操作，确保用户登录所使用的Token不被重复使用
        // 如果existingSecurityAction不为空，说明该Token已经被使用过，告知用户其已失效
        // 若为空，则说明该Token没有被使用过，可以继续下一步
        if (!isSecurityActionValid(timestamp)) {
            throw BaseException.newException(MessageType.ERROR, "用户登录所使用的Token已失效，请尝试重新登录。");
        } else {
            securityOnceActionFlag = timestamp;
        }

        // 检查用户的登录Token是否合法
        loginUser = checkUserAuthentication(username, userpwd);

        // 颁发jwtToken给登录用户，用于其后续请求
        String jwtToken = null;
        Long userId = null;
        String userName = null;
        if (loginUser != null) {
            String userMail = loginUser.getEmail();
            userName = (loginUser.getName() != null) ? loginUser.getName() : userMail;
            userId = loginUser.getId();
            jwtToken = generateJwtToken(userName, userMail);
        }

        // 记录本次用户安全操作
        if (securityOnceActionFlag != null) {
            recordSecurityAction(SecurityActionType.LOGIN, securityOnceActionFlag, userId, userName);
        }

        //更新用户上次访问时间
        if (jwtToken != null) {
            updateLastVisitTime(userName);
        }

        metricService.recordLogin(userName);
        return jwtToken;
    }

    /**
     * 颁发用户的Json Web Token，用于brower客户端调用后端服务，执行请求，该token有指定有效期
     *
     * @param userName 颁发给指定用户的用户名
     * @param userMail 颁发给指定用户的邮箱地址
     * @return
     */
    private String generateJwtToken(String userName, String userMail) {
        String jwtToken = null;

        try {
            Calendar c = Calendar.getInstance();
            Integer hour = c.get(Calendar.HOUR);
            c.set(Calendar.HOUR, hour + envProperty.JWT_SIGN_EXPIRES);
            Date expiresAt = c.getTime();

            String userRoles = null;

            //从数据库中读取用户角色
            UserEntity userEntity = findUserByName(userName);
            if (userEntity.getPassword() != null) {
                if (userEntity.getRoles() != null) {
                    userRoles = userEntity.getRoles();
                }
            }

            Algorithm algorithm = Algorithm.HMAC256(envProperty.JWT_SIGN_SECRET);
            jwtToken = JWT.create()
                    .withIssuer(envProperty.JWT_SIGN_ISSUER)
                    .withSubject(userName)
                    .withClaim("user_mail", userMail)
                    .withClaim("user_role", userRoles)
                    .withExpiresAt(expiresAt)
                    .sign(algorithm);
        } catch (UnsupportedEncodingException | JWTCreationException exception) {
            //UTF-8 encoding not supported
            log.error("failed to generate the jwt token.", exception);
        }

        return jwtToken;
    }

    /**
     * 检查用户登录信息
     */
    @Override
    public UserEntity checkUserAuthentication(String userName, String password) {
        UserEntity loginUser = checkUserLoginByEmail(userName, password);
        if (loginUser == null) {
            loginUser = checkUserLoginByName(userName, password);
        }

        if (loginUser == null) {
            loginUser = checkUserLoginByLdap(userName, password);
        }

        return loginUser;
    }

    /**
     * 检查用户登陆是否合法（通过数据库）
     * 在数据库中对[用户名+密码]进行查询匹配，若找到，则登录合法，否则非法
     *
     * @param userName 用户名
     * @param password 用户密码
     * @return 返回检查结果，如果合法，则返回用户详细信息，否则返回null
     */
    private UserEntity checkUserLoginByName(String userName, String password) {
        Boolean bSuccess = false;

        UserEntity currentUser = userRepo.findOneByName(userName);
        if (currentUser != null) {
            bSuccess = checkLoginPassword(currentUser, password);
        }

        return bSuccess ? currentUser : null;
    }

    /**
     * 检查用户登陆是否合法（通过ldap）
     * 通过ldap对[用户名+密码]进行查询验证，若验证通过，则登录合法，否则非法
     *
     * @param userName 用户名
     * @param password 用户密码
     * @return 返回检查结果，如果合法，则返回用户详细信息，否则返回null
     */
    private UserEntity checkUserLoginByLdap(String userName, String password) {
        UserVO userVO = ldapService.login(userName, password);
        if (userVO == null) {
            throw BaseException.newException(MessageType.ERROR, "用户名或密码不正确，请尝试重新登录。");
        }

        // save user entity into database
        UserEntity loginUser = ConvertUtil.convert(userVO, UserEntity.class);
        String name = loginUser.getName();
        String email = loginUser.getEmail();
        UserEntity userEntity = findUserByName(name);
        if (userEntity == null) {
            loginUser = addUser(name, email);
        }

        return loginUser;
    }

    /**
     * 检查用户登陆是否合法，通过用户邮箱+密码登录
     *
     * @param email    用户邮箱
     * @param password 用户密码
     * @return 返回检查结果，如果合法，则返回用户详细信息，否则返回null
     */
    public UserEntity checkUserLoginByEmail(String email, String password) {
        Boolean bSuccess = false;

        UserEntity currentUser = userRepo.findOneByEmail(email);
        if (currentUser != null) {
            bSuccess = checkLoginPassword(currentUser, password);
        }

        return bSuccess ? currentUser : null;
    }

    private Boolean checkLoginPassword(UserEntity user, String password) {
        String checkCode = user.getCheckcode();
        String loginPwd = Md5Tool.getStringMd5(password + checkCode);
        String userPwd = user.getPassword();
        return loginPwd.equals(userPwd);
    }

    /**
     * 获取随机密码
     *
     * @return 返回生成的随机密码
     */
    private String getRandomPassword() {
        return pwdGenerator.generatePassword(8, pwdRules);
    }

    /**
     * 获取随机校验码
     *
     * @return 返回生成的校验码
     */
    private String getCheckingCode() {
        return pwdGenerator.generatePassword(8, pwdRules);
    }

    public Boolean isValidEmailAddress(String email) {
        Boolean isValid = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            isValid = false;
        }
        return isValid;
    }

    /**
     * 检查用户是否存在
     *
     * @param userName 用户名
     * @return 若存在则返回True，否则返回False
     */
    @Override
    public Boolean hasUser(String userName) {
        return userRepo.countByName(userName) > 0;
    }

    /**
     * 通过用户名查找用户
     *
     * @param userName 用户名
     * @return 返回用户实体
     */
    @Override
    public UserEntity findUserByName(String userName) {
        return userRepo.findOneByName(userName);
    }

    /**
     * 添加用户
     *
     * @param userName 用户名
     * @return 返回新增的用户信息
     */
    public UserEntity addUser(String userName) {
        UserEntity user = null;

        if (!hasUser(userName)) {
            user = new UserEntity();
            user.setName(userName);
            user.setEmail(userName + "@ppdaicorp.com");
            user.setLastVisitAt(new Date());
            userRepo.save(user);
        }

        return user;
    }

    public UserEntity addUser(String userName, String userMail) {
        UserEntity user = new UserEntity();
        user.setName(userName);
        user.setEmail(userMail);
        user.setLastVisitAt(new Date());
        user = userRepo.save(user);
        return user;
    }

    /**
     * 更新用户的最近访问时间
     *
     * @param userName 用户名
     */
    @Override
    public void updateLastVisitTime(String userName) {
        UserEntity user = userRepo.findOneByName(userName);
        if (user != null) {
            // last visit time is updated automatically by database
            user.setLastVisitAt(new Date());
            userRepo.save(user);
        }
    }

    /**
     * 获取用户列表
     *
     * @return 返回用户列表
     */
    @Override
    public List<UserVO> getAllUsers() {
        List<UserEntity> users = userRepo.findAll();
        return ConvertUtil.convert(users, UserVO.class);
    }

    /**
     * 获取分页用户列表
     *
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageVO<UserVO> getUsersByPage(String name, int page, int size) {
        PageVO<UserVO> pageVO = new PageVO<>();
        Pageable pageable = new PageRequest(page, size);
        Page<UserEntity> userPage = userRepo.fuzzyFindByName(name, pageable);
        List<UserVO> userVOList = ConvertUtil.convert(userPage.getContent(), UserVO.class);
        pageVO.setContent(userVOList);
        pageVO.setTotalElements(userPage.getTotalElements());
        return pageVO;
    }
}
