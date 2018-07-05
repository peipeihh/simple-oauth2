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
package com.pphh.oauth.idp.local;

import com.pphh.oauth.core.identity.DefaultIdentity;
import com.pphh.oauth.core.identity.Identity;
import com.pphh.oauth.core.idp.Authentication;
import com.pphh.oauth.core.idp.AuthenticationProvider;
import com.pphh.oauth.dao.ClientRepository;
import com.pphh.oauth.dao.UserRepository;
import com.pphh.oauth.po.ClientEntity;
import com.pphh.oauth.po.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * local authentication provider
 *
 * @author huangyinhuang
 * @date 7/2/2018
 */
public class LocalAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private ClientRepository clientRepo;

    public Identity loadClientIdentity(String clientId) {
        DefaultIdentity identity = null;

        ClientEntity client = clientRepo.findByIdEx(clientId);
        if (client != null) {
            identity = new DefaultIdentity(clientId);
            UserEntity user = client.getOwner();
            if (user != null) {
                identity.setEmail(user.getEmail());
                identity.setRole(user.getRoles());
                identity.setOrganzation(null);
            }
        }

        return identity;
    }

    @Override
    public Identity load(String userName) {
        DefaultIdentity identity = null;

        UserEntity user = userRepo.findOneByName(userName);
        if (user != null) {
            identity = new DefaultIdentity(userName);
            identity.setEmail(user.getEmail());
            identity.setRole(user.getRoles());
            identity.setOrganzation(null);
        }

        return identity;
    }

    @Override
    public Identity load(Authentication authentication) {
        return load(authentication.getPrincipal().toString());
    }

    @Override
    public Class<? extends AuthenticationProvider> support() {
        return this.getClass();
    }
}
