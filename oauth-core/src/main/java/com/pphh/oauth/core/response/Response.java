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
public class Response<T> {

    private Integer code;
    private String message;
    private T details;

    public static Response mark(MessageType type) {
        return mark(type, null);
    }

    public static <T> Response<T> mark(MessageType type, T details) {
        Response<T> response = new Response<>();
        response.code = type.getCode();
        response.message = type.getMsg();
        response.details = details;
        return response;
    }

    public static Response<String> mark(MessageType type, String message, Object... params) {
        Response response;

        if (params != null && params.length > 0) {
            String formatMessage = String.format(message, params);
            response = Response.mark(type, formatMessage);
        } else {
            response = Response.mark(type, message);
        }

        return response;
    }

    public static <T> Response<T> success(T details) {
        Response<T> response = new Response<>();
        response.code = MessageType.SUCCESS.getCode();
        response.message = MessageType.SUCCESS.getMsg();
        response.details = details;
        return response;
    }

    public static <T> Response<T> error(String message, T details) {
        Response<T> response = new Response<>();
        response.code = MessageType.ERROR.getCode();
        response.message = message;
        response.details = details;
        return response;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getDetails() {
        return details;
    }

    public void setDetails(T result) {
        this.details = result;
    }

}
