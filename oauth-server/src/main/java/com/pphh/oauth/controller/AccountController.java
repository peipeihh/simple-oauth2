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
package com.pphh.oauth.controller;

import com.pphh.oauth.core.response.Response;
import com.pphh.oauth.core.response.MessageType;
import com.pphh.oauth.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 用户账号的注册和登录接口
 *
 * @author huangyinhuang
 * @date 7/5/2018
 */
@RestController
@RequestMapping("/api/account")
@Slf4j
public class AccountController {

    @Autowired
    private UserService userService;

    /**
     * 使用用户邮箱进行注册
     *
     * @param email 用户邮箱地址
     * @return 返回注册结果
     */
    @ApiOperation(value = "注册用户", notes = "使用用户邮箱进行注册")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public Response<String> register(@RequestParam(value = "email") String email) {
        Boolean bSuccess = userService.register(email);
        return bSuccess ? Response.mark(MessageType.SUCCESS, "用户注册成功，请查看邮件。") : Response.mark(MessageType.ERROR, "对不起，用户注册失败。");
    }

    /**
     * 使用用户邮箱更新用户的密码
     *
     * @param email 用户邮箱地址
     * @return 返回结果
     */
    @ApiOperation(value = "更新用户密码", notes = "根据Token信息更新密码")
    @RequestMapping(value = "/refreshPassword", method = RequestMethod.POST)
    public Response<String> refreshPassword(@RequestParam(value = "email") String email) {
        Boolean bSuccess = userService.refreshPassword(email);
        return bSuccess ? Response.mark(MessageType.SUCCESS, "用户密码已更新，请查看邮件。") : Response.mark(MessageType.ERROR, "用户密码更新失败。");
    }

    /**
     * 根据Token信息设置密码
     *
     * @param token 密码设置信息
     * @return
     */
    @ApiOperation(value = "设置用户密码", notes = "根据Token信息设置密码")
    @RequestMapping(value = "/setPassword", method = RequestMethod.POST)
    public Response<String> setUserPasswordByToken(@RequestBody String token) {
        Boolean bSuccess = userService.setPasswordByToken(token);
        return bSuccess ? Response.mark(MessageType.SUCCESS, "用户密码设置成功。") : Response.mark(MessageType.ERROR, "用户密码设置失败。");
    }

    /**
     * 使用用户邮箱设置用户名
     *
     * @param email    用户邮箱地址
     * @param username 用户名
     * @return
     */
    @ApiOperation(value = "设置用户名", notes = "根据用户邮箱设置用户名")
    @RequestMapping(value = "/setUserName", method = RequestMethod.POST)
    public Response<String> setUserName(@RequestParam(value = "email") String email, @RequestParam(value = "username") String username) {
        String jwtToken = userService.setUserName(email, username);
        return jwtToken != null ? Response.mark(MessageType.SUCCESS, jwtToken) : Response.mark(MessageType.ERROR, "用户名设置失败。");
    }

    /**
     * 根据用户登录信息Token进行登录验证，如果登录成功，则颁发JwtToken
     *
     * @param loginToken 用户登录信息
     * @return 如果登录成功，返回JwtToken
     */
    @ApiOperation(value = "用户登录接口", notes = "根据用户登录信息Token进行登录验证，如果登录成功，则颁发JwtToken")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Response<String> loginByToken(@RequestBody String loginToken) {
        String jwtToken = userService.loginUserByToken(loginToken);
        return jwtToken != null ? Response.mark(MessageType.SUCCESS, jwtToken) : Response.mark(MessageType.ERROR, "对不起，用户登录失败。");
    }

}
