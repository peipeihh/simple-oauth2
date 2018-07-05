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
package com.pphh.oauth.vo;

import com.pphh.oauth.core.constant.GrantType;
import lombok.Data;

/**
 * GrantRequestVO
 *
 * @author huangyinhuang
 * @date 7/3/2018
 */
@Data
public class GrantRequestVO {
    GrantType grant_type;
    String code;
    String refresh_token;

    public GrantType getGrantType() {
        return grant_type;
    }

    public String getCode() {
        return code;
    }

    public String getRefreshToken() {
        return refresh_token;
    }
}
