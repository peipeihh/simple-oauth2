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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pphh.oauth.po.AuditLogEntity;
import com.pphh.oauth.service.impl.AuditService;
import com.pphh.oauth.utils.RequestContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * record user's action and access as for future audit
 *
 * @author huangyinhuang
 * @date 7/2/2018
 */
@Aspect
@Component
@Slf4j
@Order(10)
public class WebLogAspect {

    @Autowired
    private AuditService auditService;

    @Before("ResourcePointCuts.apiController()")
    public void logAccessInfo(JoinPoint joinPoint) throws Throwable {
        log.info("logAccessInfo");
    }

    @Around("ResourcePointCuts.apiController()")
    public Object logAccessAudit(ProceedingJoinPoint apiMethod) throws Throwable {
        log.info("logAccessAudit");

        Object retVal = apiMethod.proceed();

        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = (HttpServletRequest) attributes.getRequest();
            String userName = RequestContextUtil.getCurrentUserName();
            String httpUri = request.getRequestURI();
            String httpMethod = request.getMethod();
            String clientIp = request.getRemoteAddr();
            String classMethod = apiMethod.getSignature().getDeclaringTypeName() + "." + apiMethod.getSignature().getName();
            String classMethodArgs = Arrays.toString(apiMethod.getArgs());

            StringBuilder rsLog = new StringBuilder();
            rsLog.append("USER NAME: " + userName);
            rsLog.append(",HTTP_METHOD : " + httpMethod);
            rsLog.append(",HTTP_URI : " + httpUri);
            rsLog.append(",IP : " + clientIp);
            rsLog.append(",CLASS_METHOD : " + classMethod);
            rsLog.append(",CLASS_METHOD_ARGS : " + classMethodArgs);

            log.info(rsLog.toString());
            AuditLogEntity action = new AuditLogEntity();
            action.setUserName(userName);
            action.setHttpMethod(httpMethod);
            action.setHttpUri(httpUri);
            action.setClientIp(clientIp);
            action.setClassMethod(classMethod);
            action.setClassMethodArgs(classMethodArgs);

            ObjectMapper mapperObj = new ObjectMapper();
            String result = mapperObj.writeValueAsString(retVal);
            result = result.length() > 1024 ? result.substring(0, 1023) : result;
            action.setClassMethodReturn(result);

            // 暂停记录访问数据到数据库（已记录为访问日志）
            //auditService.recordOperation(action);
        } catch (Exception e) {
            log.info("An exception happened when trying to log access info.");
        }

        return retVal;
    }

}
