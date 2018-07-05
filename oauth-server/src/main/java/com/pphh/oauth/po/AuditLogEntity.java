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
import java.util.Date;

/**
 * AuditLogEntity
 *
 * @author huangyinhuang
 * @date 7/3/2018
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Cacheable(false)
@Table(name = "audit_log")
public class AuditLogEntity {

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "insert_time", insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    protected Date insertTime;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_time", insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    protected Date updateTime;
    @Column(name = "is_active", nullable = false, columnDefinition = "TINYINT(1)")
    protected Boolean isActive = true;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "user_name", nullable = true, length = 32)
    private String userName;

    @Column(name = "client_ip", nullable = true, length = 32)
    private String clientIp;

    @Column(name = "http_method", nullable = true, length = 64)
    private String httpMethod;

    @Column(name = "http_uri", nullable = true, length = 256)
    private String httpUri;

    @Column(name = "class_method", nullable = true, length = 128)
    private String classMethod;

    @Column(name = "class_method_args", nullable = true, length = 256)
    private String classMethodArgs;

    @Column(name = "class_method_return", nullable = true, length = 1024)
    private String classMethodReturn;

}
