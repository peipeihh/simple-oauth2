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

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * ApprovedSiteEntity
 *
 * @author huangyinhuang
 * @date 7/3/2018
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "approved_site", schema = "", catalog = "")
public class ApprovedSiteEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Basic
    @Column(name = "client_id", nullable = false, length = 256)
    private String clientId;

    @Basic
    @Column(name = "user_name", nullable = false, length = 128)
    private String userName;

    @JsonInclude()
    @Transient
    private Iterable<ApprovedScopeEntity> scopes;

}
