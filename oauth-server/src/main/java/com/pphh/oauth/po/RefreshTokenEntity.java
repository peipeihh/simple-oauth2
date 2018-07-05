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
import org.springframework.security.oauth2.common.OAuth2RefreshToken;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * RefreshTokenEntity
 *
 * @author huangyinhuang
 * @date 7/3/2018
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "refresh_token", schema = "", catalog = "")
public class RefreshTokenEntity extends BaseEntity implements OAuth2RefreshToken {

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
    @Column(name = "auth_holder_id", nullable = true)
    private Long authHolderId;

    @Basic
    @Column(name = "user_id", nullable = true)
    private Long userId;

    @Basic
    @Column(name = "client_id", nullable = false)
    private String clientId;

    @Override
    public String getValue() {
        return this.tokenValue;
    }

}
