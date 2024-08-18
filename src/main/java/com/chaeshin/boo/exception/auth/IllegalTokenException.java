package com.chaeshin.boo.exception.auth;

/**
 * 토큰 자체에 문제가 있는 경우.
 */
public class IllegalTokenException extends TokenException {
    public IllegalTokenException() {
        super();
    }

    public IllegalTokenException(String message) {
        super(message);
    }

    public IllegalTokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalTokenException(Throwable cause) {
        super(cause);
    }

    protected IllegalTokenException(String message, Throwable cause, boolean enableSuppression,
                                    boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
