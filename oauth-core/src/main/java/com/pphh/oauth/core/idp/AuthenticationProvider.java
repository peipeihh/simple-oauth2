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
package com.pphh.oauth.core.idp;

import com.pphh.oauth.core.identity.Identity;

/**
 * The authentication provider, which helps to load identity by id/authentication
 *
 * @author huangyinhuang
 * @date 7/2/2018
 */
public interface AuthenticationProvider {

    /**
     * load identity by its id
     *
     * @param id identity id
     * @return identity object, return null if not found
     */
    Identity load(String id);

    /**
     * load identity by its authentication
     *
     * @param authentication identity authentication
     * @return identity object, return null if not found
     */
    Identity load(Authentication authentication);

    /**
     * the provider class supported
     *
     * @return supported class
     */
    Class<? extends AuthenticationProvider> support();

}
