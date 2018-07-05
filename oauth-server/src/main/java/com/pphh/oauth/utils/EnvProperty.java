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
package com.pphh.oauth.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * a util to read environment property
 *
 * @author huangyinhuang
 * @date 7/2/2018
 */
@Component
@Slf4j
public class EnvProperty {

    public static String HEADER_AUDIT_USERNAME = "app.audit.username";

    @Value("${app.jwt.check.enable:true}")
    public Boolean JWT_CHECK_ENABLE;

    @Value("${app.jwt.sign.secret:secret}")
    public String JWT_SIGN_SECRET;

    @Value("${app.jwt.sign.issuer:oauth2}")
    public String JWT_SIGN_ISSUER;

    @Value("${app.jwt.sign.expires:168}")
    public Integer JWT_SIGN_EXPIRES;

    @Value("${app.jwt.check.skip.uri:}")
    public String JWT_CHECK_SKIP_URI;

    @Value("${app.oauth.expiration.code:600000}")
    public Integer OAUTH_EXPIRE_CODE;

    @Value("${app.oauth.expiration.refreshtoken:3600000}")
    public Integer OAUTH_EXPIRE_REFRESH;

    @Value("${app.oauth.expiration.access:3600000}")
    public Integer OAUTH_EXPIRE_ACCESS;

    @Autowired
    private Environment environment;

    public String getProperty(String property) {
        return environment.getProperty(property);
    }

}
