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
package com.pphh.oauth.service;

import com.pphh.oauth.core.constant.GrantType;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 服务运营指标数据收集，包括登录次数，授权次数，授权校验次数等
 *
 * @author huangyinhuang
 * @date 7/3/2018
 */
public interface MetricService {

    public void recordLogin(String userName);

    public void recordAuthCode(String userName, String clientId);

    public void recordAuthToken(String userName, String clientId, GrantType type);

    public void recordIntrospect();

    public void recordRevoke();

}
