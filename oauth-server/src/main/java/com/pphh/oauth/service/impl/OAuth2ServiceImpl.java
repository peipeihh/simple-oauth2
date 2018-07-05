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

import com.pphh.oauth.core.constant.AuthorizeResponseType;
import com.pphh.oauth.core.constant.GrantType;
import com.pphh.oauth.core.response.MessageType;
import com.pphh.oauth.dao.AuthorizationCodeRepository;
import com.pphh.oauth.dao.ClientRepository;
import com.pphh.oauth.exception.UnAuthorizeException;
import com.pphh.oauth.oauth2.OAuth2AuthCodeServiceImpl;
import com.pphh.oauth.oauth2.OAuth2AuthTokenServiceImpl;
import com.pphh.oauth.po.AuthorizationCodeEntity;
import com.pphh.oauth.po.ClientEntity;
import com.pphh.oauth.po.UserEntity;
import com.pphh.oauth.service.MetricService;
import com.pphh.oauth.service.OAuth2Service;
import com.pphh.oauth.service.UserService;
import com.pphh.oauth.vo.AuthCodeVO;
import com.pphh.oauth.vo.ClientVO;
import com.pphh.oauth.vo.ValidityVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * OAuth2ServiceImpl
 *
 * @author huangyinhuang
 * @date 7/5/2018
 */
@Service
public class OAuth2ServiceImpl implements OAuth2Service {

    @Autowired
    private ClientRepository clientRepo;
    @Autowired
    private OAuth2AuthCodeServiceImpl oAuth2AuthCodeService;
    @Autowired
    private OAuth2AuthTokenServiceImpl oAuth2AuthTokenService;
    @Autowired
    private UserService userService;
    @Autowired
    private AuthorizationCodeRepository authCodeRepo;
    @Autowired
    private MetricService metricService;

    @Override
    public AuthCodeVO authorize(String userName, ClientVO clientVO) {
        AuthCodeVO codeVO = null;

        // TODO: check user name and password with ldap, Note: currently this step is skipped as the user has been verified by filter
        // TODO: check if the user has the access to client

        // check client id
        String clientId = clientVO.getClientId();
        ClientEntity client = clientRepo.findByIdEx(clientId);
        if (client != null) {
            // TODO: check scopes, Note: currently there are fixed scopes by definition
            Set<String> scopes = clientVO.getScopes();

            // verify client redirect url
            // Note: skip the redirect url verification when in test environment
            String redirectUrl = clientVO.getRedirectUrl();

            // redirectUrl的正则表达式校验
            boolean isMatch = false;
            try {
                isMatch = Pattern.matches(client.getRedirectUrl(), redirectUrl);
            } catch (PatternSyntaxException e) {
                throw new UnAuthorizeException(MessageType.ERROR, "应用回调地址校验异常，请确保注册的地址格式正确。");
            }

            if (isMatch) {

                // start to issue an auth code
                OAuth2Request oAuth2Request = new OAuth2Request(null, clientId, null, true, scopes, null, null, null, null);
                String userPwd = null;
                UsernamePasswordAuthenticationToken userAuthentication = new UsernamePasswordAuthenticationToken(userName, userPwd);
                OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Request, userAuthentication);

                codeVO = new AuthCodeVO();
                codeVO.setRedirectUrl(redirectUrl);
                codeVO.setUserName(userName);
                codeVO.setClientName(clientId);

                if (AuthorizeResponseType.CODE.name().equalsIgnoreCase(clientVO.getRespType())) {
                    // issue auth code
                    String code = oAuth2AuthCodeService.createAuthorizationCode(oAuth2Authentication);
                    codeVO.setCode(code);
                    metricService.recordAuthCode(userName, clientId);
                } else if (AuthorizeResponseType.TOKEN.name().equalsIgnoreCase(clientVO.getRespType())) {
                    // issue access token by implicit mode
                    OAuth2Authentication o2Authentication = oAuth2AuthCodeService.issueAuthentication(userName, null, clientId, scopes);
                    OAuth2AccessToken o2AccessToken = oAuth2AuthTokenService.createAccessToken(o2Authentication);
                    codeVO.setCode(o2AccessToken.getValue());
                    metricService.recordAuthToken(userName, clientId, GrantType.IMPLICIT);
                }

            }
        }

        return codeVO;
    }

    @Override
    public OAuth2AccessToken issueToken(String code, String clientId, String clientSecret) {
        // check if the client own the auth code
        AuthorizationCodeEntity codeEntity = authCodeRepo.findByCode(code);
        if (!clientId.equals(codeEntity.getClientId())) {
            throw new UnAuthorizeException(MessageType.ERROR, "Client does not own the presented auth code.");
        }

        // consume authorization code
        OAuth2Authentication o2Authentication = oAuth2AuthCodeService.consumeAuthorizationCode(code);

        // check the client id/secret
        ClientEntity client = clientRepo.findByIdEx(clientId);
        if (client == null || clientSecret == null || !clientSecret.equals(client.getClientSecret())) {
            throw new UnAuthorizeException(MessageType.ERROR, "Client不存在，或者Client Secret有误。");
        }

        // issue access/refresh token
        OAuth2AccessToken o2AccessToken = oAuth2AuthTokenService.createAccessToken(o2Authentication);
        metricService.recordAuthToken(null, clientId, GrantType.AUTHORIZATION_CODE);
        return o2AccessToken;
    }

    @Override
    public OAuth2AccessToken issueToken(String username, String password, String clientId, String clientSecret) {
        Set<String> scopes = new HashSet<>();
        OAuth2Authentication o2Authentication = oAuth2AuthCodeService.issueAuthentication(username, password, clientId, scopes);

        // check the user name and password
        UserEntity userEntity = userService.checkUserAuthentication(username, password);
        if (userEntity == null) {
            throw new UnAuthorizeException(MessageType.ERROR, "用户不存在，或者用户登录信息有误。");
        }

        // check the client id/secret
        ClientEntity client = clientRepo.findByIdEx(clientId);
        if (client == null || clientSecret == null || !clientSecret.equals(client.getClientSecret())) {
            throw new UnAuthorizeException(MessageType.ERROR, "Client不存在，或者Client Secret有误。");
        }

        OAuth2AccessToken o2AccessToken = oAuth2AuthTokenService.createAccessToken(o2Authentication);
        metricService.recordAuthToken(username, clientId, GrantType.PASSWORD);
        return o2AccessToken;
    }

    @Override
    public OAuth2AccessToken issueToken(String clientId, String clientSecret) {
        // check the client id/secret
        ClientEntity client = clientRepo.findByIdEx(clientId);
        if (client == null || clientSecret == null || !clientSecret.equals(client.getClientSecret())) {
            throw new UnAuthorizeException(MessageType.ERROR, "Client不存在，或者Client Secret有误。");
        }

        Set<String> scopes = new HashSet<>();
        OAuth2Authentication o2Authentication = oAuth2AuthCodeService.issueAuthentication(null, null, clientId, scopes);

        OAuth2AccessToken o2AccessToken = oAuth2AuthTokenService.createAccessToken(o2Authentication);
        metricService.recordAuthToken(null, clientId, GrantType.CLIENT_CREDENTIALS);
        return o2AccessToken;
    }

    @Override
    public OAuth2AccessToken refreshToken(String token, String clientId, String clientSecret) {
        // check the client id/secret
        ClientEntity client = clientRepo.findByIdEx(clientId);
        if (client == null || clientSecret == null || !clientSecret.equals(client.getClientSecret())) {
            throw new UnAuthorizeException(MessageType.ERROR, "Client不存在，或者Client Secret有误。");
        }

        // refresh access token
        TokenRequest tokenRequest = new TokenRequest(null, clientId, null, null);
        OAuth2AccessToken o2AccessToken = oAuth2AuthTokenService.refreshAccessToken(token, tokenRequest);
        metricService.recordAuthToken(null, clientId, GrantType.REFRESH_TOKEN);
        return o2AccessToken;
    }

    @Override
    public ValidityVO introspectToken(String token) {
        Object tokenEntity = null;
        tokenEntity = oAuth2AuthTokenService.getAccessToken(token);

        ValidityVO tokenValidity = new ValidityVO();
        tokenValidity.setIsValid(tokenEntity != null);
        metricService.recordIntrospect();
        return tokenValidity;
    }

    @Override
    public Boolean revokeToken(String token) {
        Boolean bSuccess = oAuth2AuthTokenService.revokeAccessToken(token);
        if (!bSuccess) {
            bSuccess = oAuth2AuthTokenService.revokeRefreshToken(token);
        }
        metricService.recordRevoke();
        return bSuccess;
    }
}
