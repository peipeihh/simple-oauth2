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

import com.pphh.oauth.po.ApprovedSiteEntity;
import com.pphh.oauth.vo.PageVO;

import java.util.Set;

/**
 * approved site service
 *
 * @author huangyinhuang
 * @date 7/3/2018
 */
public interface ApprovedSiteService {

    /**
     * fetch the list of sites approved by user
     *
     * @param username user name
     * @return the list of approved sites, returns empty list if there is no approved site
     */
    Iterable<ApprovedSiteEntity> getSitesByUserName(String username);

    /**
     * get the page list of approved sites by user
     *
     * @param username user name
     * @param clientId client id
     * @param page     page index
     * @param size     page size
     * @return the list of approved sites, returns empty list if there is no approved site
     */
    PageVO<ApprovedSiteEntity> getSitesByPage(String username, String clientId, Integer page, Integer size);

    /**
     * save a approved site by user
     *
     * @param userName user name
     * @param clientId client id
     * @param scopes   approved scopes
     * @return approved site by saved
     */
    ApprovedSiteEntity record(String userName, String clientId, Set<String> scopes);

    /**
     * remove a site by id
     *
     * @param siteId site id
     */
    void removeSiteById(Long siteId);

    /**
     * check if there is any approval for client's site by user
     *
     * @param username user name
     * @param clientId client id
     * @return true if there is existing approval
     */
    Boolean existApprovalFor(String username, String clientId);

}
