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

import com.pphh.oauth.utils.RequestContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * initializer of user audit, which will be used by spring jpa audit feature
 *
 * @author huangyinhuang
 * @date 7/2/2018
 */
@Configuration
@EnableJpaAuditing
@Slf4j
public class UserAuditorAware implements AuditorAware<String> {

    public static final String DEFAULT_SYSTEM_NAME = "system";

    @Override
    public String getCurrentAuditor() {
        String userName = RequestContextUtil.getCurrentUserName();

        if (userName == null) {
            userName = DEFAULT_SYSTEM_NAME;
        }

        return userName;
    }

}
