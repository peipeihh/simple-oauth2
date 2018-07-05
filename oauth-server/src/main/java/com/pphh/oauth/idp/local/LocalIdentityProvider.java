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

import com.pphh.oauth.core.idp.AuthenticationProvider;
import com.pphh.oauth.core.idp.IdentityProvider;
import com.pphh.oauth.core.idp.IdentityProviderConfiguration;

/**
 * local identity provider
 *
 * @author huangyinhuang
 * @date 7/2/2018
 */
public class LocalIdentityProvider implements IdentityProvider {

    @Override
    public Class<? extends IdentityProviderConfiguration> configuration() {
        return LocalProviderConfiguration.class;
    }

    @Override
    public Class<? extends AuthenticationProvider> authenticationProvider() {
        return LocalAuthenticationProvider.class;
    }

}
