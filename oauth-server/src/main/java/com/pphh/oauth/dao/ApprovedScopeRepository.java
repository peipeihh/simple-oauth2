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

import com.pphh.oauth.po.ApprovedScopeEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * approved scope repository
 *
 * @author huangyinhuang
 * @date 7/3/2018
 */
public interface ApprovedScopeRepository extends CrudRepository<ApprovedScopeEntity, Long> {

    @Query("SELECT e FROM ApprovedScopeEntity e WHERE e.isActive=true and e.approvedSiteId=?1")
    Iterable<ApprovedScopeEntity> findBySiteId(Long siteId);

}
