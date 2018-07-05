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

import com.pphh.oauth.core.response.Response;
import com.pphh.oauth.core.response.MessageType;
import com.pphh.oauth.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Handle all the exceptions that are thrown from controller/service/dao
 *
 * @author huangyinhuang
 * @date 7/2/2018
 */
@Aspect
@Component
@Slf4j
@Order(11)
public class ExceptionAspect {

    @Around("ResourcePointCuts.webController()")
    public Object handleWebException(ProceedingJoinPoint apiMethod) {
        log.info("try to handle exception thrown from web controller method");

        Object retVal = null;
        try {
            retVal = apiMethod.proceed();
        } catch (BaseException e) {
            log.info(e.getMessage());
            retVal = Response.mark(e.getMessageType(), e.getMessage());
        } catch (Throwable throwable) {
            UUID uuid = UUID.randomUUID();
            String msg = String.format("[%s] %s", uuid, throwable.getMessage());
            log.error(msg, throwable);
            retVal = Response.mark(MessageType.UNKNOWN, "未知错误，请联系负责团队寻求更多帮助，定位GUID为[" + uuid + "]。");
        }

        return retVal;
    }

}
