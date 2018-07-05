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
package com.pphh.oauth.manager;

import com.pphh.oauth.po.AccessTokenEntity;
import com.pphh.oauth.po.AuthorizationCodeEntity;
import com.pphh.oauth.po.ClientEntity;
import com.pphh.oauth.po.RefreshTokenEntity;
import com.pphh.oauth.service.AuthHolderService;
import com.pphh.oauth.service.ClientService;
import com.pphh.oauth.utils.ConvertUtil;
import com.pphh.oauth.vo.AuthCodeVO;
import com.pphh.oauth.vo.AuthTokenVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * MapService
 *
 * @author huangyinhuang
 * @date 7/3/2018
 */
@Service
public class EntityMapper {

    @Autowired
    private ClientService clientService;
    @Autowired
    private AuthHolderService authHolderService;

    public AuthCodeVO mapAuthCode(AuthorizationCodeEntity authCode) {
        AuthCodeVO authCodeVO = ConvertUtil.convert(authCode, AuthCodeVO.class);

        Long authHolderId = authCode.getAuthHolderId();
        String userName = authHolderService.getUserNameByHolderId(authHolderId);
        authCodeVO.setUserName(userName);

        String clientName = authHolderService.getClientNameByHolderId(authHolderId);
        authCodeVO.setClientName(clientName);

        ClientEntity client = clientService.findByClientName(clientName);
        if (client != null) {
            authCodeVO.setRedirectUrl(client.getRedirectUrl());
        }

        return authCodeVO;
    }

    public AuthTokenVO mapAccessToken(AccessTokenEntity accessToken) {
        String clientId = accessToken.getClientId();
        Long authHolderId = accessToken.getAuthHolderId();
        return build(accessToken, clientId, authHolderId);
    }

    public AuthTokenVO mapRefreshToken(RefreshTokenEntity refreshToken) {
        String clientId = refreshToken.getClientId();
        Long authHolderId = refreshToken.getAuthHolderId();

        return build(refreshToken, clientId, authHolderId);
    }

    private AuthTokenVO build(Object source, String clientId, Long authHolderId) {
        AuthTokenVO tokenVO = new AuthTokenVO();
        BeanUtils.copyProperties(source, tokenVO);
        if (clientId != null) {
            tokenVO.setClientName(clientId);
        }
        if (authHolderId != null) {
            tokenVO.setUserName(authHolderService.getUserNameByHolderId(authHolderId));
        }
        return tokenVO;
    }
}
