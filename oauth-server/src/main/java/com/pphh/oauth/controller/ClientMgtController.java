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

import com.pphh.oauth.core.response.MessageType;
import com.pphh.oauth.core.response.Response;
import com.pphh.oauth.manager.ClientManager;
import com.pphh.oauth.manager.UserOwnedRrcManager;
import com.pphh.oauth.po.ClientEntity;
import com.pphh.oauth.service.ApprovedSiteService;
import com.pphh.oauth.service.ClientService;
import com.pphh.oauth.utils.RequestContextUtil;
import com.pphh.oauth.vo.ClientCheckResultVO;
import com.pphh.oauth.vo.ClientVO;
import com.pphh.oauth.vo.PageVO;
import com.pphh.oauth.vo.ValidityVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 应用开发者的Client注册接口，申请Client Id和Secret
 *
 * @author huangyinhuang
 * @date 7/5/2018
 */
@RestController
@RequestMapping("/api/clients")
public class ClientMgtController {


    @Autowired
    private ClientService clientService;
    @Autowired
    private UserOwnedRrcManager userOwnedRrcManager;
    @Autowired
    private ClientManager clientManager;
    @Autowired
    private ApprovedSiteService approvedSiteService;

    @ApiOperation(value = "获取client列表")
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Response<List<ClientEntity>> getClientList(@RequestParam(value = "username", required = false) String username) {
        List<ClientEntity> clients;
        if (username == null || username.isEmpty()) {
            clients = clientService.fetchAllClients();
        } else {
            clients = userOwnedRrcManager.getMyClients(username);
        }
        return Response.mark(MessageType.SUCCESS, clients);
    }

    @ApiOperation(value = "获取分页client列表")
    @RequestMapping(method = RequestMethod.GET)
    public Response<PageVO<ClientEntity>> getClientsByPage(@RequestParam String clientId,
                                                           @RequestParam Long ownerId,
                                                           @RequestParam Integer page,
                                                           @RequestParam Integer size) {
        PageVO<ClientEntity> clientPageVO = clientService.fetchClientsByPage(clientId, ownerId, page, size);
        return Response.mark(MessageType.SUCCESS, clientPageVO);
    }

    @ApiOperation(value = "创建client")
    @RequestMapping(method = RequestMethod.POST)
    public Response<String> createNewClient(@RequestBody ClientVO clientVO) {
        clientService.register(clientVO);
        return Response.mark(MessageType.SUCCESS, "应用[%s]注册成功。", clientVO.getClientId().trim());
    }

    @ApiOperation(value = "更新client")
    @RequestMapping(value = "/{clientId}", method = RequestMethod.PUT)
    public Response<String> updateClientById(@PathVariable Long clientId, @RequestBody ClientEntity client) {
        clientService.updateById(client);
        return Response.mark(MessageType.SUCCESS, "应用[id=%s]信息更新成功。", clientId);
    }

    @ApiOperation(value = "删除client")
    @RequestMapping(value = "/{clientId}", method = RequestMethod.DELETE)
    public Response<String> removeClientById(@PathVariable Long clientId) {
        clientService.removeById(clientId);
        return Response.mark(MessageType.SUCCESS, "应用[id=%s]已经删除成功。", clientId);
    }

    @ApiOperation(value = "校验client的合法性")
    @RequestMapping(value = "/introspect", method = RequestMethod.POST)
    public Response<ClientCheckResultVO> verifyClient(@RequestBody ClientVO clientVO) {
        ClientCheckResultVO checkResult = new ClientCheckResultVO();

        ValidityVO validityVO = clientManager.introspectClient(clientVO);
        checkResult.setIsValid(validityVO.getIsValid());

        //在client校验为合法时，检查是否可以直接进行授权
        if (validityVO.getIsValid()) {
            String userName = RequestContextUtil.getCurrentUserName();
            Boolean existApproval = approvedSiteService.existApprovalFor(userName, clientVO.getClientId());
            checkResult.setDirectApprove(existApproval);
        }

        return Response.mark(MessageType.SUCCESS, checkResult);
    }

}
