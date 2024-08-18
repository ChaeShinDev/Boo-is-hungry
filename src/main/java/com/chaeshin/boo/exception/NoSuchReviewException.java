package com.chaeshin.boo.exception;

public class NoSuchReviewException extends RuntimeException {
    public NoSuchReviewException() {
        super();
    }

    public NoSuchReviewException(String message) {
        super(message);
    }

    public NoSuchReviewException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchReviewException(Throwable cause) {
        super(cause);
    }

    protected NoSuchReviewException(String message, Throwable cause, boolean enableSuppression,
                                    boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
