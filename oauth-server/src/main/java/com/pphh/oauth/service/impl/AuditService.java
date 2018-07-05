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

import com.pphh.oauth.dao.AuditLogRepository;
import com.pphh.oauth.exception.BaseException;
import com.pphh.oauth.po.AuditLogEntity;
import com.pphh.oauth.vo.PageVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 审计服务：记录各种操作日志，用于后续审计
 *
 * @author huangyinhuang
 * @date 7/3/2018
 */
@Service
@Slf4j
public class AuditService {

    @Autowired
    AuditLogRepository auditLogRepo;

    @Transactional(rollbackFor = BaseException.class)
    public void recordOperation(AuditLogEntity actionItem) {
        auditLogRepo.save(actionItem);
    }

    public PageVO<AuditLogEntity> fetchAuditLogsByPage(int page, int size) {
        PageVO<AuditLogEntity> pageVO = new PageVO<>();
        Pageable pageable = new PageRequest(page, size);
        Page<AuditLogEntity> auditLogPage = auditLogRepo.findAll(pageable);
        pageVO.setContent(auditLogPage.getContent());
        pageVO.setTotalElements(auditLogPage.getTotalElements());
        return pageVO;
    }

}
