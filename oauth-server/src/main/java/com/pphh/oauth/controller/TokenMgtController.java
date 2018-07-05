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
import com.pphh.oauth.manager.TokenManager;
import com.pphh.oauth.manager.UserOwnedRrcManager;
import com.pphh.oauth.vo.AuthCodeVO;
import com.pphh.oauth.vo.AuthTokenVO;
import com.pphh.oauth.vo.PageVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * OAuth2 access token/refresh token的后台管理接口
 *
 * @author huangyinhuang
 * @date 7/5/2018
 */
@RestController
@RequestMapping("/api")
public class TokenMgtController {

    @Autowired
    private TokenManager tokenManager;
    @Autowired
    private UserOwnedRrcManager userOwnedRrcManager;

    @ApiOperation(value = "获取access token列表")
    @RequestMapping(value = "/access_tokens/all", method = RequestMethod.GET)
    public Response<List<AuthTokenVO>> getAccessTokenList(@RequestParam(value = "username", required = false) String username) {
        List<AuthTokenVO> accessTokens = new ArrayList<>();
        if (username == null) {
            accessTokens = tokenManager.getAllAccessTokens();
        } else if (!username.equals("null")) {
            accessTokens = userOwnedRrcManager.getMyAccessTokens(username);
        }
        return Response.mark(MessageType.SUCCESS, accessTokens);
    }

    @ApiOperation(value = "获取access token分页列表")
    @RequestMapping(value = "/access_tokens", method = RequestMethod.GET)
    public Response<PageVO<AuthTokenVO>> getAccessTokensByPage(@RequestParam Long userId,
                                                               @RequestParam String clientId,
                                                               @RequestParam Integer page,
                                                               @RequestParam Integer size) {
        PageVO<AuthTokenVO> accessTokenPageVO = tokenManager.getAccessTokensByPage(userId, clientId, page, size);
        return Response.mark(MessageType.SUCCESS, accessTokenPageVO);
    }

    @ApiOperation(value = "吊销access token")
    @RequestMapping(value = "/access_tokens/{tokenId}", method = RequestMethod.DELETE)
    public Response<String> revokeAccessTokenById(@PathVariable Long tokenId) {
        tokenManager.revokeAccessToken(tokenId);
        return Response.mark(MessageType.SUCCESS, "访问令牌id=%s已经被注销", tokenId);
    }

    @ApiOperation(value = "获取refresh token列表")
    @RequestMapping(value = "/refresh_tokens/all", method = RequestMethod.GET)
    public Response<List<AuthTokenVO>> getRefreshTokenList(
            @RequestParam(value = "username", required = false) String username) {
        List<AuthTokenVO> refreshTokens = new ArrayList<>();
        if (username == null) {
            refreshTokens = tokenManager.getAllRefreshTokens();
        } else if (!username.equals("null")) {
            refreshTokens = userOwnedRrcManager.getMyRefreshTokens(username);
        }
        return Response.mark(MessageType.SUCCESS, refreshTokens);
    }

    @ApiOperation(value = "获取refresh token分页列表")
    @RequestMapping(value = "/refresh_tokens", method = RequestMethod.GET)
    public Response<PageVO<AuthTokenVO>> getRefreshTokensByPage(@RequestParam Long userId,
                                                                @RequestParam String clientId,
                                                                @RequestParam Integer page,
                                                                @RequestParam Integer size) {
        PageVO<AuthTokenVO> refreshTokenPageVO = tokenManager.getRefreshTokensByPage(userId, clientId, page, size);
        return Response.mark(MessageType.SUCCESS, refreshTokenPageVO);
    }

    @ApiOperation(value = "吊销refresh token")
    @RequestMapping(value = "/refresh_tokens/{tokenId}", method = RequestMethod.DELETE)
    public Response<String> revokeRefreshTokenById(@PathVariable Long tokenId) {
        tokenManager.revokeRefreshToken(tokenId);
        return Response.mark(MessageType.SUCCESS, "刷新令牌id=%s已经被注销", tokenId);
    }

    @ApiOperation(value = "获取auth codes列表")
    @RequestMapping(value = "/auth_codes/all", method = RequestMethod.GET)
    public Response<List<AuthCodeVO>> getAuthCodeList(
            @RequestParam(value = "username", required = false) String username) {
        List<AuthCodeVO> authCodes = new ArrayList<>();
        if (username == null) {
            authCodes = tokenManager.getAllAuthCodes();
        } else if (!username.equals("null")) {
            authCodes = userOwnedRrcManager.getMyAuthCodes(username);
        }
        return Response.mark(MessageType.SUCCESS, authCodes);
    }

    @ApiOperation(value = "获取auth code分页列表")
    @RequestMapping(value = "/auth_codes", method = RequestMethod.GET)
    public Response<PageVO<AuthCodeVO>> getAuthCodesByPage(@RequestParam Long userId,
                                                           @RequestParam String clientId,
                                                           @RequestParam Integer page,
                                                           @RequestParam Integer size) {
        PageVO<AuthCodeVO> authCodePageVO = tokenManager.getAuthCodesByPage(userId, clientId, page, size);
        return Response.mark(MessageType.SUCCESS, authCodePageVO);
    }

    @ApiOperation(value = "吊销auth code")
    @RequestMapping(value = "/auth_codes/{codeId}", method = RequestMethod.DELETE)
    public Response<String> revokeAuthCodeById(@PathVariable Long codeId) {
        tokenManager.revokeAuthCode(codeId);
        return Response.mark(MessageType.SUCCESS, "授权码id=%s已经被注销", codeId);
    }
}
