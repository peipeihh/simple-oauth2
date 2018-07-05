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
package com.pphh.oauth.exception;

import com.pphh.oauth.core.response.MessageType;

/**
 * BaseException
 *
 * @author huangyinhuang
 * @date 7/2/2018
 */
public class BaseException extends RuntimeException {

    private MessageType msgType = MessageType.UNKNOWN;

    public BaseException(MessageType msgType) {
        this.msgType = msgType;
    }

    public BaseException(MessageType msgType, Throwable cause) {
        super(cause);
        this.msgType = msgType;
    }

    public BaseException(MessageType msgType, String message) {
        super(message);
        this.msgType = msgType;
    }

    public BaseException(MessageType msgType, Throwable cause, String message) {
        super(message, cause);
        this.msgType = msgType;
    }

    public BaseException(MessageType msgType, String details, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(details, cause, enableSuppression, writableStackTrace);
        this.msgType = msgType;
    }

    public static BaseException newException(MessageType msgType, String message, Object... params) {
        BaseException exception;
        if (params != null && params.length > 0) {
            String formatMessage = String.format(message, params);
            if (params[params.length - 1] instanceof Throwable) {
                exception = new BaseException(msgType, (Throwable) params[params.length - 1], formatMessage);
            } else {
                exception = new BaseException(msgType, formatMessage);
            }
        } else {
            exception = new BaseException(msgType, message);
        }
        return exception;
    }

    public MessageType getMessageType() {
        return this.msgType;
    }

}
