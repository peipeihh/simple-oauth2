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

import com.pphh.oauth.service.MailService;
import com.pphh.oauth.utils.EnvProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * SmtpMailServiceImpl
 *
 * @author huangyinhuang
 * @date 7/5/2018
 */
@Service
public class SmtpMailServiceImpl implements MailService {

    @Autowired
    private EnvProperty envProperty;

    @Override
    public Boolean sendMail(String from, String to, String subject, String content) {

        Boolean bSuccess = false;

        try {
            // 创建mime类型邮件
            Session session = Session.getDefaultInstance(getProperties());
            MimeMessage message = new MimeMessage(session);

            InternetAddress sender = new InternetAddress(from);
            message.setFrom(sender);
            message.setRecipients(javax.mail.Message.RecipientType.TO, to);
            message.setSubject(subject);
            message.setText(content);

            Transport.send(message);
            bSuccess = true;
        } catch (Exception ex) {
            ex.printStackTrace();
            bSuccess = false;
        }

        return bSuccess;
    }

    private Properties getProperties() {
        Properties props = new Properties();
        props.setProperty("mail.smtp.host", envProperty.getProperty("mail.smtp.host"));
        props.setProperty("mail.smtp.port", envProperty.getProperty("mail.smtp.port"));
        props.setProperty("mail.smtp.auth", envProperty.getProperty("mail.smtp.auth"));
        return props;
    }

}
