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

import com.pphh.oauth.core.response.MessageType;
import com.pphh.oauth.core.response.Response;
import com.pphh.oauth.service.UserService;
import com.pphh.oauth.vo.PageVO;
import com.pphh.oauth.vo.UserVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 用户的后台管理接口
 *
 * @author huangyinhuang
 * @date 7/5/2018
 */
@RestController
@RequestMapping("/api/users")
public class UserMgtController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "获取用户列表")
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Response<List<UserVO>> getAllUsers() {
        List<UserVO> users = userService.getAllUsers();
        return Response.mark(MessageType.SUCCESS, users);
    }

    @ApiOperation(value = "获取分页用户列表")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public Response<PageVO<UserVO>> getUsersByPage(@RequestParam String name,
                                                   @RequestParam Integer page,
                                                   @RequestParam Integer size) {
        PageVO<UserVO> userPageVO = userService.getUsersByPage(name, page, size);
        return Response.mark(MessageType.SUCCESS, userPageVO);
    }

}
