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
package com.pphh.oauth.core.utils;

import java.nio.charset.Charset;
import java.util.Base64;

/**
 * Please add description here.
 *
 * @author huangyinhuang
 * @date 7/2/2018
 */
public class BasicAuthUtil {

    public static String BASIC_PREFIX = "Basic";

    public static String[] decode(String authorization) {
        String[] values = null;

        if (authorization != null && authorization.startsWith(BASIC_PREFIX)) {
            // Authorization: Basic base64credentials
            String base64Credentials = authorization.substring(BASIC_PREFIX.length()).trim();
            String credentials = new String(Base64.getDecoder().decode(base64Credentials), Charset.forName("UTF-8"));
            // credentials = username:password
            values = credentials.split(":", 2);
        }
        return values;

    }

    public static String encode(String id, String secret) {
        String str = String.format("%s:%s", id, secret);
        str = Base64.getEncoder().encodeToString(str.getBytes());
        return String.format("%s %s", BASIC_PREFIX, str);
    }

}
