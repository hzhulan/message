package com.hzhu.wechat.model;

/**
 * @description:
 * @author: pp_lan
 * @date: 2021/11/2 16:57
 */
public class CommonException extends RuntimeException {

    public CommonException() {
    }

    public CommonException(String message) {
        super(message);
    }

    public CommonException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommonException(Throwable cause) {
        super(cause);
    }

    public CommonException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
