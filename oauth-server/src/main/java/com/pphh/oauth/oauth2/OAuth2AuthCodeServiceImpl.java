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
package com.pphh.oauth.oauth2;

import com.pphh.oauth.core.response.MessageType;
import com.pphh.oauth.dao.AuthenticationHolderRepository;
import com.pphh.oauth.dao.AuthorizationCodeRepository;
import com.pphh.oauth.dao.ClientRepository;
import com.pphh.oauth.dao.UserRepository;
import com.pphh.oauth.exception.BaseException;
import com.pphh.oauth.exception.UnAuthorizeException;
import com.pphh.oauth.po.AuthenticationHolderEntity;
import com.pphh.oauth.po.AuthorizationCodeEntity;
import com.pphh.oauth.po.ClientEntity;
import com.pphh.oauth.po.UserEntity;
import com.pphh.oauth.scheduler.OAuth2TokenExpireUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.util.RandomValueStringGenerator;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

/**
 * A implementation of authorization code service
 *
 * @author huangyinhuang
 * @date 7/3/2018
 */
@Service
public class OAuth2AuthCodeServiceImpl implements AuthorizationCodeServices {

    @Autowired
    private AuthenticationHolderRepository authHolderRepo;
    @Autowired
    private AuthorizationCodeRepository authCodeRepo;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private ClientRepository clientRepo;
    @Autowired
    private OAuth2TokenExpireUtil tokenExpireUtil;

    private RandomValueStringGenerator generator;

    public OAuth2AuthCodeServiceImpl() {
        generator = new RandomValueStringGenerator();
    }

    @Override
    public String createAuthorizationCode(OAuth2Authentication o2Authentication) {
        String userName = (String) o2Authentication.getPrincipal();
        UserEntity user = userRepo.findOneByName(userName);

        // create authorization holder
        AuthenticationHolderEntity authHolder = new AuthenticationHolderEntity();
        authHolder.setAuthentication(o2Authentication);
        authHolder.setUserId(user.getId());

        String clientId = authHolder.getClientId();
        ClientEntity client = clientRepo.findByIdEx(clientId);
        if (client != null) {
            authHolder.setRedirectUri(client.getRedirectUrl());
        }
        authHolderRepo.save(authHolder);

        // issue authorization code
        String randomVal = generator.generate();
        AuthorizationCodeEntity codeEntity = new AuthorizationCodeEntity();
        codeEntity.setAuthHolderId(authHolder.getId());
        codeEntity.setCode(randomVal);
        Timestamp expiration = tokenExpireUtil.getOAuthCodeExpireTime();
        codeEntity.setExpiration(expiration);
        codeEntity.setUserId(user.getId());
        codeEntity.setClientId(clientId);
        authCodeRepo.save(codeEntity);

        return randomVal;
    }

    @Transactional(rollbackFor = BaseException.class)
    @Override
    public OAuth2Authentication consumeAuthorizationCode(String code) throws InvalidGrantException {
        // remove any expired auth code
        tokenExpireUtil.removeAuthCode();

        // check the code
        AuthorizationCodeEntity codeEntity = authCodeRepo.findByCode(code);
        if (codeEntity == null) {
            throw UnAuthorizeException.newException(MessageType.ERROR, "授权码无效：%s", code);
        }

        // remove the auth code
        authCodeRepo.removeById(codeEntity.getId());

        Long authHolderId = codeEntity.getAuthHolderId();
        AuthenticationHolderEntity authHolder = authHolderRepo.findOne(authHolderId);

        String clientId = authHolder.getClientId();
        Long userId = authHolder.getUserId();
        UserEntity user = userRepo.findOne(userId);
        String userName = user.getName();

        Set<String> scopes = new HashSet<>();
        OAuth2Authentication oAuth2Authentication = issueAuthentication(userName, null, clientId, scopes);
        return oAuth2Authentication;
    }

    public OAuth2Authentication issueAuthentication(String userName, String password, String clientId, Set<String> scopes) {
        OAuth2Request oAuth2Request = new OAuth2Request(null, clientId, null, true, scopes, null, null, null, null);
        UsernamePasswordAuthenticationToken userAuthentication = null;
        if (userName != null) {
            userAuthentication = new UsernamePasswordAuthenticationToken(userName, password);
        }
        return new OAuth2Authentication(oAuth2Request, userAuthentication);
    }

}
