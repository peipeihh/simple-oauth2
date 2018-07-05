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
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;

import javax.persistence.*;

/**
 * AuthenticationHolderEntity
 *
 * @author huangyinhuang
 * @date 7/3/2018
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "authentication_holder", schema = "", catalog = "")
public class AuthenticationHolderEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Basic
    @Column(name = "user_id", nullable = true)
    private Long userId;

    @Basic
    @Column(name = "approved", nullable = true)
    private Boolean approved;

    @Basic
    @Column(name = "redirect_uri", nullable = true, length = 2048)
    private String redirectUri;

    @Basic
    @Column(name = "client_id", nullable = true, length = 256)
    private String clientId;

    public void setAuthentication(OAuth2Authentication o2Authentication) {
        OAuth2Request o2Request = o2Authentication.getOAuth2Request();
        this.setClientId(o2Request.getClientId());
        this.setApproved(o2Request.isApproved());
        this.setRedirectUri(o2Request.getRedirectUri());
    }

}
