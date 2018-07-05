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
package com.pphh.oauth.config;

import com.pphh.oauth.utils.EnvProperty;
import com.pphh.oauth.utils.LdapProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * ldap configuration
 *
 * @author huangyinhuang
 * @date 7/2/2018
 */
@Configuration
public class LDAPConfiguration {

    /**
     * envProperty is used to fetch environment settings
     */
    @Autowired
    private EnvProperty envProperty;

    /**
     * ldapProperty is implemented by ConfigurationProperties, which is initialized at app startup
     */
    @Autowired
    private LdapProperty ldapProperty;

    public String getServer() {
        return envProperty.getProperty("app.ldap.server");
    }

    public List<String> getPaths() {
        String[] paths = null;
        if (ldapProperty.getSearch() != null) {
            paths = ldapProperty.getSearch().getPaths();
        }

        if (paths != null) {
            return Arrays.asList(ldapProperty.getSearch().getPaths());
        } else {
            return new ArrayList<>();
        }

    }

}
