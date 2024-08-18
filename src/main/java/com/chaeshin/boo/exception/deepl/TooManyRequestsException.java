package com.chaeshin.boo.exception.deepl;

public class TooManyRequestsException extends DeepLException {
    public TooManyRequestsException() {
        super();
    }

    public TooManyRequestsException(String message) {
        super(message);
    }

    public TooManyRequestsException(String message, Throwable cause) {
        super(message, cause);
    }

    public TooManyRequestsException(Throwable cause) {
        super(cause);
    }

    protected TooManyRequestsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
