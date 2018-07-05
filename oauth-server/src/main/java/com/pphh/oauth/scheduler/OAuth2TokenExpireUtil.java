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
package com.pphh.oauth.scheduler;

import com.pphh.oauth.dao.AccessTokenRepository;
import com.pphh.oauth.dao.AuditLogRepository;
import com.pphh.oauth.dao.AuthorizationCodeRepository;
import com.pphh.oauth.dao.RefreshTokenRepository;
import com.pphh.oauth.exception.BaseException;
import com.pphh.oauth.po.AccessTokenEntity;
import com.pphh.oauth.po.RefreshTokenEntity;
import com.pphh.oauth.utils.EnvProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

/**
 * token clean task
 *
 * @author huangyinhuang
 * @date 7/3/2018
 */
@Component
public class OAuth2TokenExpireUtil {

    @Autowired
    private AuthorizationCodeRepository authCodeRepo;
    @Autowired
    private AccessTokenRepository accessTokenRepo;
    @Autowired
    private RefreshTokenRepository refreshTokenRepo;
    @Autowired
    private AuditLogRepository auditLogRepo;

    @Autowired
    private EnvProperty envProperty;

    @Transactional(rollbackFor = BaseException.class)
    public void removeAuthCode() {
        authCodeRepo.removeByTimeBefore(this.getNow());
    }

    @Transactional(rollbackFor = BaseException.class)
    public void removeAccessToken() {
        accessTokenRepo.removeByTimeBefore(this.getNow());
    }

    @Transactional(rollbackFor = BaseException.class)
    public void removeRefreshToken() {
        refreshTokenRepo.removeByTimeBefore(this.getNow());
    }

    @Transactional(rollbackFor = BaseException.class)
    public void cleanAuthCode() {
        authCodeRepo.deleteByTimeBefore(this.getNow());
    }

    @Transactional(rollbackFor = BaseException.class)
    public void cleanAccessToken() {
        accessTokenRepo.deleteByTimeBefore(this.getNow());
    }

    @Transactional(rollbackFor = BaseException.class)
    public void cleanRefreshToken() {
        /**
         * 注：不能使用refreshTokenRepo.deleteByTimeBefore(this.getNow())直接删除所有的过期refresh token
         * 原因是有些refresh token过期了，但是access token还未过期失效
         * 正确清理方法：找到所有过期的refresh token，检查是否有未过期的access token，若没有，则删除该refresh token
         */
        Iterable<RefreshTokenEntity> refreshTokens = refreshTokenRepo.findInactiveTokenByTimeBefore(this.getNow());
        for (RefreshTokenEntity refreshToken : refreshTokens) {
            Iterable<AccessTokenEntity> accessTokens = accessTokenRepo.findByRefreshToken(refreshToken);
            // 若所有相关的access token已删除，则可以清理该refresh token
            if (!accessTokens.iterator().hasNext()) {
                refreshTokenRepo.delete(refreshToken);
            }
        }
    }

    /**
     * 清理15天前的审计日志
     */
    @Transactional(rollbackFor = BaseException.class)
    public void cleanAuditLog() {
        Timestamp expired = this.getTime(-15 * 24 * 60 * 60 * 1000L);
        auditLogRepo.deleteByTimeBefore(expired);
    }

    public Timestamp getOAuthCodeExpireTime() {
        return new Timestamp(System.currentTimeMillis() + envProperty.OAUTH_EXPIRE_CODE);
    }

    public Timestamp getAccessTokenExpireTime() {
        return new Timestamp(System.currentTimeMillis() + envProperty.OAUTH_EXPIRE_ACCESS);
    }

    public Timestamp getRefreshTokenExpireTime() {
        return new Timestamp(System.currentTimeMillis() + envProperty.OAUTH_EXPIRE_REFRESH);
    }

    public Timestamp getNow() {
        return new Timestamp(System.currentTimeMillis());
    }

    public Timestamp getTime(Long diff) {
        return new Timestamp(System.currentTimeMillis() + diff);
    }


}
