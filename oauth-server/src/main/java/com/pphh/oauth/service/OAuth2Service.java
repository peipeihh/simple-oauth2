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

import com.pphh.oauth.vo.AuthCodeVO;
import com.pphh.oauth.vo.ClientVO;
import com.pphh.oauth.vo.ValidityVO;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

/**
 * OAuth2Service
 *
 * @author huangyinhuang
 * @date 7/3/2018
 */
public interface OAuth2Service {

    AuthCodeVO authorize(String userName, ClientVO clientVO);

    /**
     * 通过授权码获取access/refresh token
     *
     * @param code         授权码
     * @param clientId     申请token的client id
     * @param clientSecret 申请token的client secret
     * @return
     */
    OAuth2AccessToken issueToken(String code, String clientId, String clientSecret);

    /**
     * 通过用户名和密码获取access/refresh token
     *
     * @param username     用户名
     * @param password     用户密码
     * @param clientId     申请token的client id
     * @param clientSecret 申请token的client secret
     * @return
     */
    OAuth2AccessToken issueToken(String username, String password, String clientId, String clientSecret);

    /**
     * 通过client id/secret直接获取access/refresh token
     *
     * @param clientId     申请token的client id
     * @param clientSecret 申请token的client secret
     * @return
     */
    OAuth2AccessToken issueToken(String clientId, String clientSecret);

    OAuth2AccessToken refreshToken(String token, String clientId, String clientSecret);

    ValidityVO introspectToken(String token);

    Boolean revokeToken(String token);

}
