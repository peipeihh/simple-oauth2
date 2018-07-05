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
package com.pphh.oauth.core.response;

/**
 * common web service response, which provide uniform format of response code and message
 *
 * @author huangyinhuang
 * @date 7/2/2018
 */
public enum MessageType {


    /**
     * 请求成功消息
     */
    SUCCESS(0, "请求成功完成。"),

    /**
     * 请求中发现错误
     */
    ERROR(-1, "发现错误。"),

    /**
     * 请求中发现未知错误
     */
    UNKNOWN(-4, "未知错误。");

    private Integer code;
    private String msg;

    MessageType(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }

}
