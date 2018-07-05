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

/**
 * MailService
 *
 * @author huangyinhuang
 * @date 7/3/2018
 */
public interface MailService {

    /**
     * 发送邮件
     *
     * @param from    发送者
     * @param to      接受者
     * @param subject 邮件标题
     * @param content 邮件内容
     * @return
     */
    Boolean sendMail(String from, String to, String subject, String content);

}
