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
package com.pphh.oauth.core.constant;

/**
 * OAuth 2.0授权模式
 *
 * @author huangyinhuang
 * @date 7/2/2018
 */
public enum GrantType {

    /**
     * OAuth2授权码模式
     */
    AUTHORIZATION_CODE,

    /**
     * OAuth2刷新码
     */
    REFRESH_TOKEN,

    /**
     * OAuth2 Client Credential模式
     */
    CLIENT_CREDENTIALS,

    /**
     * OAuth2 Resource Owner Password模式
     */
    PASSWORD,

    /**
     * OAuth2简化模式
     */
    IMPLICIT,

}
