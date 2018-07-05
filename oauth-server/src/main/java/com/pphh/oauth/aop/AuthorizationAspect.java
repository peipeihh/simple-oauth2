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

import com.pphh.oauth.config.UserAuditorAware;
import com.pphh.oauth.service.UserService;
import com.pphh.oauth.utils.RequestContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * check user's information and its permission
 *
 * @author huangyinhuang
 * @date 7/2/2018
 */
@Aspect
@Component
@Slf4j
@Order(12)
public class AuthorizationAspect {

    @Autowired
    private UserService userService;

    @Before("ResourcePointCuts.apiController()")
    public void checkPermission(JoinPoint joinPoint) throws Throwable {
        // read user name from request attribute
        String userName = RequestContextUtil.getCurrentUserName();

        if (userName != null && !userName.equals(UserAuditorAware.DEFAULT_SYSTEM_NAME)) {
            // if the user name
            //    - doesn't exist in the database, add user into database
            //    - exit in the database, update the visit time
            if (!userService.hasUser(userName)) {
                log.info("add user into database");
                //authService.addUser(userName);
            } else {
                userService.updateLastVisitTime(userName);
            }
        }

        // TODO: check user's permission when user permission is ready
    }
}
