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
package com.pphh.oauth.aop;

import org.aspectj.lang.annotation.Pointcut;

/**
 * Define the pointcuts for the spring aop handler
 *
 * @author huangyinhuang
 * @date 7/2/2018
 */
public class ResourcePointCuts {

    /**
     * all rest api, which includes web controller and oauth2 endpoint
     */
    @Pointcut("execution(public * com.pphh.oauth.controller..*.*(..))")
    public void apiController() {
    }

    /**
     * web controller
     */
    @Pointcut("execution(public * com.pphh.oauth.controller..*Controller.*(..))")
    public void webController() {
    }

    /**
     * oauth2 endpoint
     */
    @Pointcut("execution(public * com.pphh.oauth.controller..*Endpoint.*(..))")
    public void oauth2Endpoint() {
    }

}
