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
import com.pphh.oauth.po.ApprovedSiteEntity;
import com.pphh.oauth.service.ApprovedSiteService;
import com.pphh.oauth.vo.PageVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * OAuth2登录session的后台管理接口
 *
 * @author huangyinhuang
 * @date 7/5/2018
 */
@RestController
@RequestMapping("/api/sessions")
@Slf4j
public class SessionMgtController {

    @Autowired
    private ApprovedSiteService approvedSiteService;

    @ApiOperation(value = "获取用户登录session列表")
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Response<Iterable<ApprovedSiteEntity>> getSessionList(@RequestParam(value = "username") String username) {
        Iterable<ApprovedSiteEntity> sites = approvedSiteService.getSitesByUserName(username);
        return Response.mark(MessageType.SUCCESS, sites);
    }

    @ApiOperation(value = "获取登录session分页列表")
    @RequestMapping(method = RequestMethod.GET)
    public Response<PageVO<ApprovedSiteEntity>> getSessionsByPage(@RequestParam String username,
                                                                  @RequestParam String clientId,
                                                                  @RequestParam Integer page,
                                                                  @RequestParam Integer size) {
        PageVO<ApprovedSiteEntity> sitePageVO = approvedSiteService.getSitesByPage(username, clientId, page, size);
        return Response.mark(MessageType.SUCCESS, sitePageVO);
    }

    @ApiOperation(value = "注销用户登录session", notes = "注销用户登录session，让用户申请token时重新验证通过。")
    @RequestMapping(value = "/{sessionId}", method = RequestMethod.DELETE)
    public Response<String> revokeSessionById(@PathVariable Long sessionId) {
        approvedSiteService.removeSiteById(sessionId);
        return Response.mark(MessageType.SUCCESS, "用户登录session[id=%s]已被注销。", sessionId);
    }


}
