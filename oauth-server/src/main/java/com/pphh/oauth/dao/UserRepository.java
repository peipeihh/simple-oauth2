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

import com.pphh.oauth.po.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * user repository
 *
 * @author huangyinhuang
 * @date 7/3/2018
 */
public interface UserRepository extends BaseJpaRepository<UserEntity, Long> {

    Long countByName(String name);

    @Query("SELECT e FROM UserEntity e WHERE e.isActive=true AND e.name=?1")
    UserEntity findOneByName(String name);

    @Query("SELECT e FROM UserEntity e WHERE e.isActive=true AND e.email=?1")
    UserEntity findOneByEmail(String email);

    Page<UserEntity> findAll(Specification<UserEntity> specification, Pageable pageable);

    @Query("select e from UserEntity e where e.isActive=true")
    @Override
    Page<UserEntity> findAll(Pageable pageable);

    @Query("select e from UserEntity e where e.isActive=true and e.name like concat('%',:name,'%')")
    Page<UserEntity> fuzzyFindByName(@Param("name") String name, Pageable pageable);

}
