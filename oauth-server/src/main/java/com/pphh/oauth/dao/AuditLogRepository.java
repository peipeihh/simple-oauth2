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
package com.pphh.oauth.dao;

import com.pphh.oauth.po.AuditLogEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.sql.Timestamp;

/**
 * audit log repository
 *
 * @author huangyinhuang
 * @date 7/3/2018
 */
public interface AuditLogRepository extends CrudRepository<AuditLogEntity, Long> {

    /**
     * 删除过期的审计日志，从数据库中永久删除字段
     *
     * @param expired 过期时间
     */
    @Modifying(clearAutomatically = true)
    @Query("delete from AuditLogEntity e where e.updateTime<=?1")
    void deleteByTimeBefore(Timestamp expired);

    @Query("select e from AuditLogEntity e where e.isActive=true and e.userName is not null")
    Page<AuditLogEntity> findAll(Pageable pageable);

}
