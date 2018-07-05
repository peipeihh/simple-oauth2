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
package com.pphh.oauth.po;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * AccessTokenEntity
 *
 * @author huangyinhuang
 * @date 7/3/2018
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "access_token", schema = "", catalog = "")
public class AccessTokenEntity extends BaseEntity implements OAuth2AccessToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Basic
    @Column(name = "token_value", nullable = true, length = 1024)
    private String tokenValue;

    @Basic
    @Column(name = "expiration", nullable = true)
    private Timestamp expiration;

    @Basic
    @Column(name = "token_type", nullable = true, length = 256)
    private String tokenType = OAuth2AccessToken.BEARER_TYPE;

//    @Basic
//    @Column(name = "refresh_token_id", nullable = true)
//    private Long refreshTokenId;

    @Basic
    @Column(name = "auth_holder_id", nullable = true)
    private Long authHolderId;

    @Basic
    @Column(name = "user_id", nullable = true)
    private Long userId;

    @Basic
    @Column(name = "client_id", nullable = false)
    private String clientId;

    @ManyToOne(targetEntity = RefreshTokenEntity.class)
    @JoinColumn(name = "refresh_token_id")
    private RefreshTokenEntity refreshToken;

    @Override
    public Map<String, Object> getAdditionalInformation() {
        return new HashMap<>(16);
    }

    @Override
    public Set<String> getScope() {
        return new HashSet<>();
    }

    @Override
    public OAuth2RefreshToken getRefreshToken() {
        return this.refreshToken;
    }

    @Override
    public boolean isExpired() {
        return getExpiration() != null && System.currentTimeMillis() > getExpiration().getTime();
    }

    @Override
    public int getExpiresIn() {
        if (getExpiration() == null) {
            // no expiration time
            return -1;
        } else {
            int secondsRemaining = (int) ((getExpiration().getTime() - System.currentTimeMillis()) / 1000);
            if (isExpired()) {
                // has an expiration time and expired
                return 0;
            } else {
                // has an expiration time and not expired
                return secondsRemaining;
            }
        }
    }

    @Override
    public String getValue() {
        return this.tokenValue;
    }
}
