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
import com.pphh.oauth.dao.AuthenticationHolderRepository;
import com.pphh.oauth.dao.AuthorizationCodeRepository;
import com.pphh.oauth.dao.RefreshTokenRepository;
import com.pphh.oauth.exception.BaseException;
import com.pphh.oauth.po.AccessTokenEntity;
import com.pphh.oauth.po.AuthorizationCodeEntity;
import com.pphh.oauth.po.RefreshTokenEntity;
import com.pphh.oauth.service.OAuth2Service;
import com.pphh.oauth.utils.ConvertUtil;
import com.pphh.oauth.vo.AuthCodeVO;
import com.pphh.oauth.vo.AuthTokenVO;
import com.pphh.oauth.vo.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * Access/Refresh Token的管理
 *
 * @author huangyinhuang
 * @date 7/3/2018
 */
@Service
public class TokenManager {


    @Autowired
    private OAuth2Service oAuth2Service;
    @Autowired
    private AuthorizationCodeRepository authCodeRepo;
    @Autowired
    private AccessTokenRepository accessTokenRepo;
    @Autowired
    private RefreshTokenRepository refreshTokenRepo;
    @Autowired
    private AuthenticationHolderRepository authenticationHolderRepo;

    @Autowired
    private EntityMapper mapperService;

    public List<AuthCodeVO> getAllAuthCodes() {
        Iterable<AuthorizationCodeEntity> authCodes = authCodeRepo.findAllEx();
        return ConvertUtil.convert(authCodes, mapperService::mapAuthCode);
    }

    public PageVO<AuthCodeVO> getAuthCodesByPage(Long userId, String clientId, Integer page, Integer size) {
        PageVO<AuthCodeVO> codePageVO = new PageVO<>();
        Pageable pageable = new PageRequest(page, size);

        Page<AuthorizationCodeEntity> codePage = authCodeRepo.findAll((root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>();
            if (userId != null) {
                list.add(criteriaBuilder.equal(root.get("userId").as(Long.class), userId));
            }
            if (!"".equals(clientId)) {
                list.add(criteriaBuilder.equal(root.get("clientId").as(String.class), clientId));
            }
            list.add(criteriaBuilder.equal(root.get("isActive").as(Boolean.class), Boolean.TRUE));
            Predicate[] p = new Predicate[list.size()];
            return criteriaBuilder.and(list.toArray(p));
        }, pageable);

        List<AuthorizationCodeEntity> codeList = codePage.getContent();
        List<AuthCodeVO> codeVOList = ConvertUtil.convert(codeList, mapperService::mapAuthCode);
        codePageVO.setContent(codeVOList);
        codePageVO.setTotalElements(codePage.getTotalElements());
        return codePageVO;
    }

    public List<AuthTokenVO> getAllAccessTokens() {
        Iterable<AccessTokenEntity> tokens = accessTokenRepo.findAllEx();
        List<AuthTokenVO> tokenVOs = ConvertUtil.convert(tokens, mapperService::mapAccessToken);
        return tokenVOs;
    }

    public PageVO<AuthTokenVO> getAccessTokensByPage(Long userId, String clientId, Integer page, Integer size) {
        PageVO<AuthTokenVO> tokenPageVO = new PageVO<>();
        Pageable pageable = new PageRequest(page, size);

        Page<AccessTokenEntity> tokenPage = accessTokenRepo.findAll((root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>();
            if (userId != null) {
                list.add(criteriaBuilder.equal(root.get("userId").as(Long.class), userId));
            }
            if (!clientId.isEmpty()) {
                list.add(criteriaBuilder.equal(root.get("clientId").as(String.class), clientId));
            }
            list.add(criteriaBuilder.equal(root.get("isActive").as(Boolean.class), Boolean.TRUE));
            Predicate[] p = new Predicate[list.size()];
            return criteriaBuilder.and(list.toArray(p));
        }, pageable);

        List<AccessTokenEntity> tokenList = tokenPage.getContent();
        List<AuthTokenVO> tokenVOList = ConvertUtil.convert(tokenList, mapperService::mapAccessToken);
        tokenPageVO.setContent(tokenVOList);
        tokenPageVO.setTotalElements(tokenPage.getTotalElements());
        return tokenPageVO;
    }

    public List<AuthTokenVO> getAllRefreshTokens() {
        Iterable<RefreshTokenEntity> tokens = refreshTokenRepo.findAllEx();
        List<AuthTokenVO> tokenVOs = ConvertUtil.convert(tokens, mapperService::mapRefreshToken);
        return tokenVOs;
    }

    public PageVO<AuthTokenVO> getRefreshTokensByPage(Long userId, String clientId, Integer page, Integer size) {
        PageVO<AuthTokenVO> tokenPageVO = new PageVO<>();
        Pageable pageable = new PageRequest(page, size);

        Page<RefreshTokenEntity> tokenPage = refreshTokenRepo.findAll((root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>();
            if (userId != null) {
                list.add(criteriaBuilder.equal(root.get("userId").as(Long.class), userId));
            }
            if (!"".equals(clientId)) {
                list.add(criteriaBuilder.equal(root.get("clientId").as(String.class), clientId));
            }
            list.add(criteriaBuilder.equal(root.get("isActive").as(Boolean.class), Boolean.TRUE));
            Predicate[] p = new Predicate[list.size()];
            return criteriaBuilder.and(list.toArray(p));
        }, pageable);

        List<RefreshTokenEntity> tokenList = tokenPage.getContent();
        List<AuthTokenVO> tokenVOList = ConvertUtil.convert(tokenList, mapperService::mapRefreshToken);
        tokenPageVO.setContent(tokenVOList);
        tokenPageVO.setTotalElements(tokenPage.getTotalElements());
        return tokenPageVO;
    }

    @Transactional(rollbackFor = BaseException.class)
    public Boolean revokeAuthCode(Long codeId) {
        authCodeRepo.removeById(codeId);
        return Boolean.TRUE;
    }

    public Boolean revokeAccessToken(Long tokenId) {
        AccessTokenEntity token = accessTokenRepo.findOne(tokenId);
        return oAuth2Service.revokeToken(token.getValue());
    }

    public Boolean revokeRefreshToken(Long tokenId) {
        RefreshTokenEntity token = refreshTokenRepo.findOne(tokenId);
        return oAuth2Service.revokeToken(token.getValue());
    }
}
