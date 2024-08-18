package com.chaeshin.boo.exception.auth;

/**
 * 토큰 내 데이터가 존재하지 않을 때 발생하는 예외.
 */
public class EmptyTokenException extends TokenException {
    public EmptyTokenException() {
        super();
    }

    public EmptyTokenException(String message) {
        super(message);
    }

    public EmptyTokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmptyTokenException(Throwable cause) {
        super(cause);
    }

    protected EmptyTokenException(String message, Throwable cause, boolean enableSuppression,
                                  boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
