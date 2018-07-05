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

import com.google.common.collect.Maps;
import com.pphh.oauth.config.LDAPConfiguration;
import com.pphh.oauth.service.LdapService;
import com.pphh.oauth.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import java.util.Map;
import java.util.Properties;

/**
 * LdapServiceImpl
 *
 * @author huangyinhuang
 * @date 7/5/2018
 */
@Slf4j
@Service
public class LdapServiceImpl implements LdapService {

    @Autowired
    private LDAPConfiguration ldapConfig;

    private Map<String, UserVO> userMap = Maps.newHashMap();

    @Override
    public UserVO login(String username, String password) {
        for (String searchPath : ldapConfig.getPaths()) {
            UserVO user = getUser(username, password, searchPath);
            if (user != null) {
                userMap.put(user.getName(), user);
                return user;
            }
        }
        return null;
    }

    private UserVO getUser(String username, String password, String searchPath) {
        UserVO user = null;
        try {

            Properties env = new Properties();

            env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
            env.put(Context.SECURITY_AUTHENTICATION, "simple");
            env.put(Context.SECURITY_PRINCIPAL, "corp\\" + username);
            env.put(Context.SECURITY_CREDENTIALS, password);
            env.put(Context.PROVIDER_URL, ldapConfig.getServer());

            LdapContext ctx = new InitialLdapContext(env, null);
            SearchControls searchCtls = new SearchControls();
            searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);

            String searchFilter = String.format("(&(objectCategory=person)(objectClass=user)(SAMAccountName=%s))",
                    username);

            String[] returnedAtts = {"memberOf", "sAMAccountName", "cn", "distinguishedName", "mail"};
            searchCtls.setReturningAttributes(returnedAtts);
            NamingEnumeration<SearchResult> answer = ctx.search(searchPath, searchFilter, searchCtls);
            if (answer.hasMoreElements()) {

                SearchResult sr = (SearchResult) answer.next();
                Attributes attribute = sr.getAttributes();
                user = new UserVO();
                user.setName(username);
                user.setEmail(getValue(attribute.get("mail")));
                ctx.close();
                return user;
            }
        } catch (NamingException e) {
            log.error(String.format("LDAP validate user %s error: %s", username, e.getMessage()), e);

        }
        return user;
    }


    private String getValue(Attribute attribute) {
        if (attribute == null) {
            return "";
        }
        String value = attribute.toString();
        if (StringUtils.isEmpty(value)) {
            return "";
        }
        if (value.indexOf(":") != -1) {
            value = value.replaceAll(value.split(":")[0], "").trim();
            value = value.substring(1, value.length()).trim();
        }
        return value;
    }

}
