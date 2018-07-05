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
package com.pphh.oauth.service;

import com.pphh.oauth.po.UserEntity;
import com.pphh.oauth.vo.PageVO;
import com.pphh.oauth.vo.UserVO;

import java.util.*;

/**
 * UserService
 *
 * @author huangyinhuang
 * @date 7/3/2018
 */

public interface UserService {

    /**
     * 使用用户邮箱进行注册，生成随机密码和校验码，最后发送邮件告知用户信息
     *
     * @param email 用户邮箱地址
     * @return 返回注册结果
     */
    public Boolean register(String email);

    /**
     * 更新密码，发送邮箱告知用户信息
     *
     * @param email 用户注册时的邮箱
     * @return 返回更新密码结果，是否成功
     */
    public Boolean refreshPassword(String email);

    /**
     * 设置登录用户名
     *
     * @param email    用户注册时的邮箱
     * @param userName 用户名
     * @return 返回用户名设置结果，是否成功
     */
    public String setUserName(String email, String userName);


    /**
     * 设置密码
     *
     * @param token 用户更改密码请求的Token
     * @return 返回更新密码结果，是否成功
     */
    public Boolean setPasswordByToken(String token);

    /**
     * 检查用户登录token是否合法，若合法，则颁发指定有效期的JWT token，用于用户发送后续请求
     *
     * @param token 登录token
     * @return 返回检查结果，如果合法，则颁发指定有效期的Json Web Token，否则返回Null
     */
    public String loginUserByToken(String token);

    /**
     * 检查用户登录信息
     */
    public UserEntity checkUserAuthentication(String userName, String password);


    /**
     * 检查用户是否存在
     *
     * @param userName 用户名
     * @return 若存在则返回True，否则返回False
     */
    public Boolean hasUser(String userName);

    /**
     * 通过用户名查找用户
     *
     * @param userName 用户名
     * @return 返回用户实体
     */
    public UserEntity findUserByName(String userName);

    /**
     * 更新用户的最近访问时间
     *
     * @param userName 用户名
     */
    public void updateLastVisitTime(String userName);

    /**
     * 获取用户列表
     *
     * @return 返回用户列表
     */
    public List<UserVO> getAllUsers();

    /**
     * 获取分页用户列表
     *
     * @param page
     * @param size
     * @return
     */
    public PageVO<UserVO> getUsersByPage(String name, int page, int size);

}
