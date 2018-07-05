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
package com.pphh.oauth.manager;

import com.pphh.oauth.core.response.MessageType;
import com.pphh.oauth.exception.BaseException;
import com.pphh.oauth.po.ClientEntity;
import com.pphh.oauth.service.ClientService;
import com.pphh.oauth.vo.ClientVO;
import com.pphh.oauth.vo.ValidityVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * ClientManager
 *
 * @author huangyinhuang
 * @date 7/3/2018
 */
@Service
public class ClientManager {

    @Autowired
    private ClientService clientService;

    /**
     * 对Client实体信息做合法性校验，包括检查ClientId, Scopes，RedirectUrl
     *
     * @param clientVO Client实体信息
     * @return 返回Client合法性检查结果
     */
    public ValidityVO introspectClient(ClientVO clientVO) {
        // 合法性检查结果，初始为False
        ValidityVO validityVO = new ValidityVO();

        // verify client id
        String clientId = clientVO.getClientId();
        ClientEntity client = clientService.findByClientName(clientId);
        if (client != null) {

            // TODO: verify client scopes, Note: currently there are fixed scopes by definition
            Set<String> scopes = clientVO.getScopes();

            // verify client redirect url
            String redirectUrl = clientVO.getRedirectUrl();

            // redirectUrl的正则表达式校验
            boolean isMatch = false;
            try {
                isMatch = Pattern.matches(client.getRedirectUrl(), redirectUrl);
            } catch (PatternSyntaxException e) {
                throw new BaseException(MessageType.ERROR, "应用回调地址校验异常，请确保注册的地址格式正确。");
            }

            validityVO.setIsValid(isMatch);
        }

        return validityVO;
    }

}
