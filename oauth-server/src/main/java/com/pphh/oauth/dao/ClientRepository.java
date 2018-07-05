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

import com.pphh.oauth.po.ClientEntity;
import com.pphh.oauth.po.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * client repository
 *
 * @author huangyinhuang
 * @date 7/3/2018
 */
public interface ClientRepository extends BaseJpaRepository<ClientEntity, Long> {

    @Query("SELECT e FROM ClientEntity e WHERE e.isActive=true")
    List<ClientEntity> findAllEx();

    @Query("SELECT e FROM ClientEntity e WHERE e.isActive=true and e.owner=?1")
    List<ClientEntity> findByOwner(UserEntity owner);

    @Query("SELECT e FROM ClientEntity e WHERE e.isActive=true and e.clientId=?1")
    ClientEntity findByIdEx(String clientId);

    @Query("SELECT e FROM ClientEntity e WHERE e.clientId=?1")
    ClientEntity findByClientId(String clientId);

    @Modifying(clearAutomatically = true)
    @Query("update ClientEntity e set e.isActive=false where e.id=?1")
    void removeById(Long id);

    Page<ClientEntity> findAll(Specification<ClientEntity> specification, Pageable pageable);

    @Query("select e from ClientEntity e where e.isActive=true")
    @Override
    Page<ClientEntity> findAll(Pageable pageable);

}
