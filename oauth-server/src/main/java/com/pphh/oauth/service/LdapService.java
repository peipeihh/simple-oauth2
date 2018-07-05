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

import com.pphh.oauth.vo.UserVO;


/**
 * Please add description here.
 *
 * @author huangyinhuang
 * @date 7/3/2018
 */

public interface LdapService {

    /**
     * 通过LDAP校验用户登录
     *
     * @param username 用户LDAP域账号
     * @param password 用户LDAP域账号密码
     * @return 用户信息
     */
    public UserVO login(String username, String password);

}
