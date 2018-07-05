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
package com.pphh.oauth.service;

import com.pphh.oauth.po.ClientEntity;
import com.pphh.oauth.po.UserEntity;
import com.pphh.oauth.vo.ClientVO;
import com.pphh.oauth.vo.PageVO;

import java.util.List;

/**
 * ClientService
 *
 * @author huangyinhuang
 * @date 7/3/2018
 */
public interface ClientService {

    void register(ClientVO clientVO);

    List<ClientEntity> fetchAllClients();

    PageVO<ClientEntity> fetchClientsByPage(String clientId, Long ownerId, int page, int size);

    List<ClientEntity> fetchClientByUser(UserEntity owner);

    void removeById(Long clientId);

    void updateById(ClientEntity client);

    String getName(Long clientId);

    ClientEntity findByClientName(String clientName);

}
