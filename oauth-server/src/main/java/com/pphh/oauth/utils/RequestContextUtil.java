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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * a utils to read user name from request context
 *
 * @author huangyinhuang
 * @date 7/2/2018
 */
@Slf4j
public class RequestContextUtil {

    public static String getCurrentUserName() {
        String userName = null;

        try {
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            userName = (String) requestAttributes.getAttribute(EnvProperty.HEADER_AUDIT_USERNAME, 0);
        } catch (Exception e) {
            log.info("Not able to read the user name by servlet requests. Probably it's a system call.");
        }

        return userName;
    }

}
