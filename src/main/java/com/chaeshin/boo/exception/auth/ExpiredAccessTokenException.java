package com.chaeshin.boo.exception.auth;

/**
 * Access Token 유효기간 만료 시의 Exception
 */
public class ExpiredAccessTokenException extends TokenException {
    public ExpiredAccessTokenException() {
        super();
    }

    public ExpiredAccessTokenException(String message) {
        super(message);
    }

    public ExpiredAccessTokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExpiredAccessTokenException(Throwable cause) {
        super(cause);
    }

    protected ExpiredAccessTokenException(String message, Throwable cause, boolean enableSuppression,
                                          boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
