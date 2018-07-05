package com.pphh.oauth.exception;

import com.pphh.oauth.core.response.MessageType;

/**
 * UnAuthorizeException
 *
 * @author huangyinhuang
 * @date 7/3/2018
 */
public class UnAuthorizeException extends BaseException {

    public UnAuthorizeException(MessageType msgType) {
        super(msgType);
    }

    public UnAuthorizeException(MessageType msgType, Throwable cause) {
        super(msgType, cause);
    }

    public UnAuthorizeException(MessageType msgType, String message) {
        super(msgType, message);
    }

    public UnAuthorizeException(MessageType msgType, Throwable cause, String message) {
        super(msgType, cause, message);
    }

    public UnAuthorizeException(MessageType msgType, String details, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(msgType, details, cause, enableSuppression, writableStackTrace);
    }

    public static UnAuthorizeException newException(MessageType msgType, String message, Object... params) {
        UnAuthorizeException exception;
        if (params != null && params.length > 0) {
            String formatMessage = String.format(message, params);
            if (params[params.length - 1] instanceof Throwable) {
                exception = new UnAuthorizeException(msgType, (Throwable) params[params.length - 1], formatMessage);
            } else {
                exception = new UnAuthorizeException(msgType, formatMessage);
            }
        } else {
            exception = new UnAuthorizeException(msgType, message);
        }
        return exception;
    }

}
