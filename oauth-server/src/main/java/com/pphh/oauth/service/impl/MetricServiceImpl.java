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
package com.pphh.oauth.service.impl;

import com.pphh.oauth.core.constant.GrantType;
import com.pphh.oauth.service.MetricService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * MetricServiceImpl
 *
 * @author huangyinhuang
 * @date 7/5/2018
 */
@Service
@Slf4j
public class MetricServiceImpl implements MetricService {

    @Override
    public void recordLogin(String userName) {
        Map<String, String> tags = new HashMap<>(8);
        tags.put("user", userName);
        counter("auth.login");
    }

    @Override
    public void recordAuthCode(String userName, String clientId) {
        Map<String, String> tags = new HashMap<>(8);
        tags.put("user", userName);
        tags.put("client", clientId);
        counter("auth.code");
    }

    @Override
    public void recordAuthToken(String userName, String clientId, GrantType type) {
        Map<String, String> tags = new HashMap<>(8);
        tags.put("user", userName);
        tags.put("client", clientId);
        tags.put("type", type.name());
        counter("auth.token");
    }

    @Override
    public void recordIntrospect() {
        Map<String, String> tags = new HashMap<>(8);
        counter("auth.introspect");
    }

    @Override
    public void recordRevoke() {
        Map<String, String> tags = new HashMap<>(8);
        counter("auth.revoke");
    }

    public void counter(String key) {
        log.info("TBD：使用时间序列数据库（openTSDB）记录事件发生 - " + key);
    }
}
