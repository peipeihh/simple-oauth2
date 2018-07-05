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

import javax.persistence.*;

/**
 * ClientEntity
 *
 * @author huangyinhuang
 * @date 7/3/2018
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "client", schema = "", catalog = "")
public class ClientEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Basic
    @Column(name = "description", nullable = true, length = 1024)
    private String description;

    @Basic
    @Column(name = "client_id", nullable = true, length = 256)
    private String clientId;

    @Basic
    @Column(name = "client_secret", nullable = true, length = 2048)
    private String clientSecret;

    @Basic
    @Column(name = "basic_auth", nullable = true, length = 256)
    private String basicAuth;

    @Basic
    @Column(name = "redirect_url", nullable = true, length = 2048)
    private String redirectUrl;

    @ManyToOne(targetEntity = UserEntity.class)
    @JoinColumn(name = "owner_id")
    private UserEntity owner;

    @Basic
    @Column(name = "reuse_refresh_tokens", nullable = false)
    private Boolean reuseRefreshTokens = false;

}
