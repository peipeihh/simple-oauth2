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

import com.pphh.oauth.po.ApprovedSiteEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * approved site repository
 *
 * @author huangyinhuang
 * @date 7/3/2018
 */
public interface ApprovedSiteRepository extends BaseJpaRepository<ApprovedSiteEntity, Long> {

    @Query("SELECT e FROM ApprovedSiteEntity e WHERE e.isActive=true and e.userName=?1")
    Iterable<ApprovedSiteEntity> findByUserName(String userName);

    @Modifying(clearAutomatically = true)
    @Query("update ApprovedSiteEntity e set e.isActive=false where e.id=?1")
    void removeById(Long id);

    @Query("SELECT e FROM ApprovedSiteEntity e WHERE e.isActive=true and e.userName=?1 and e.clientId=?2")
    Iterable<ApprovedSiteEntity> findByUserNameAndClientId(String userName, String clientId);

    Page<ApprovedSiteEntity> findAll(Specification<ApprovedSiteEntity> specification, Pageable pageable);

}
