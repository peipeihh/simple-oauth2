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
package com.pphh.oauth.controller;

import com.pphh.oauth.core.response.Response;
import com.pphh.oauth.core.response.MessageType;
import com.pphh.oauth.po.AuditLogEntity;
import com.pphh.oauth.service.impl.AuditService;
import com.pphh.oauth.vo.PageVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * AuditController
 *
 * @author huangyinhuang
 * @date 7/5/2018
 */
@RestController
@RequestMapping("/api/audit")
public class AuditController {

    @Autowired
    private AuditService auditService;

    @ApiOperation(value = "获取分页audit_log列表")
    @RequestMapping(method = RequestMethod.GET)
    public Response<PageVO<AuditLogEntity>> getAuditLogsByPage(@RequestParam Integer page,
                                                               @RequestParam Integer size) {
        PageVO<AuditLogEntity> auditLogPageVO = auditService.fetchAuditLogsByPage(page, size);
        return Response.mark(MessageType.SUCCESS, auditLogPageVO);
    }

}
