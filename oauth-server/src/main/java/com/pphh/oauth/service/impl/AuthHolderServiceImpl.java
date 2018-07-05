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
package com.pphh.oauth.service.impl;

import com.pphh.oauth.dao.AuthenticationHolderRepository;
import com.pphh.oauth.dao.ClientRepository;
import com.pphh.oauth.dao.UserRepository;
import com.pphh.oauth.po.AuthenticationHolderEntity;
import com.pphh.oauth.po.UserEntity;
import com.pphh.oauth.service.AuthHolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * AuthHolderServiceImpl
 *
 * @author huangyinhuang
 * @date 7/5/2018
 */
@Service
public class AuthHolderServiceImpl implements AuthHolderService {

    @Autowired
    private AuthenticationHolderRepository authHolderRepo;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private ClientRepository clientRepo;

    @Override
    public String getUserNameByHolderId(Long holderId) {
        String userName = null;

        if (holderId != null) {
            AuthenticationHolderEntity authHolder = authHolderRepo.findOne(holderId);
            if (authHolder != null) {
                Long userId = authHolder.getUserId();
                if (userId != null) {
                    UserEntity user = userRepo.findOne(userId);
                    userName = user.getName();
                    if (userName == null) {
                        userName = user.getEmail();
                    }
                }
            }
        }

        return userName;
    }

    @Override
    public String getClientNameByHolderId(Long holderId) {
        String clientName = null;

        if (holderId != null) {
            AuthenticationHolderEntity authHolder = authHolderRepo.findOne(holderId);
            if (authHolder != null) {
                clientName = authHolder.getClientId();
            }
        }

        return clientName;
    }

}
