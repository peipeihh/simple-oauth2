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

import com.pphh.oauth.dao.AccessTokenRepository;
import com.pphh.oauth.dao.AuthorizationCodeRepository;
import com.pphh.oauth.dao.RefreshTokenRepository;
import com.pphh.oauth.dao.UserRepository;
import com.pphh.oauth.po.*;
import com.pphh.oauth.service.*;
import com.pphh.oauth.utils.ConvertUtil;
import com.pphh.oauth.vo.AuthCodeVO;
import com.pphh.oauth.vo.AuthTokenVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 获取用户所拥有的信息，包括：授权码、Access/Refresh Token、Clients、授权通过的登录sessions
 *
 * @author huangyinhuang
 * @date 7/3/2018
 */
@Service
public class UserOwnedRrcManager {

    @Autowired
    private AuthorizationCodeRepository authCodeRepo;
    @Autowired
    private AccessTokenRepository accessTokenRepo;
    @Autowired
    private RefreshTokenRepository refreshTokenRepo;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private ClientService clientService;
    @Autowired
    private UserService userService;
    @Autowired
    private AuthHolderService authHolderService;
    @Autowired
    private ApprovedSiteService approvedSiteService;

    @Autowired
    private EntityMapper mapperService;

    public List<ApprovedSiteEntity> getMyApprovedSites(String username) {
        return (List<ApprovedSiteEntity>) approvedSiteService.getSitesByUserName(username);
    }

    public List<ClientEntity> getMyClients(String username) {
        UserEntity owner = userService.findUserByName(username);
        return clientService.fetchClientByUser(owner);
    }

    public List<AuthCodeVO> getMyAuthCodes(String username) {
        UserEntity user = userRepo.findOneByName(username);
        Iterable<AuthorizationCodeEntity> authCodeList = authCodeRepo.findByUserId(user.getId());
        List<AuthCodeVO> authCodeVOList = ConvertUtil.convert(authCodeList, mapperService::mapAuthCode);
        return authCodeVOList;
    }

    public List<AuthTokenVO> getMyAccessTokens(String username) {
        UserEntity user = userRepo.findOneByName(username);
        Iterable<AccessTokenEntity> accessTokenList = accessTokenRepo.findByUserId(user.getId());
        List<AuthTokenVO> authTokenVOList = ConvertUtil.convert(accessTokenList, mapperService::mapAccessToken);
        return authTokenVOList;
    }

    public List<AuthTokenVO> getMyRefreshTokens(String username) {
        UserEntity user = userRepo.findOneByName(username);
        Iterable<RefreshTokenEntity> refreshTokenList = refreshTokenRepo.findByUserId(user.getId());
        List<AuthTokenVO> authTokenVOList = ConvertUtil.convert(refreshTokenList, mapperService::mapRefreshToken);
        return authTokenVOList;
    }


}
