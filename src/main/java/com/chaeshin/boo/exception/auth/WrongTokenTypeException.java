package com.chaeshin.boo.exception.auth;

/**
 * Access Token 검증 로직에 refresh token이 들어가거나 반대 상황인 경우 발생.
 */
public class WrongTokenTypeException extends TokenException {

    public WrongTokenTypeException() {
        super();
    }

    public WrongTokenTypeException(String message) {
        super(message);
    }

    public WrongTokenTypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public WrongTokenTypeException(Throwable cause) {
        super(cause);
    }

    protected WrongTokenTypeException(String message, Throwable cause, boolean enableSuppression,
                                      boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
