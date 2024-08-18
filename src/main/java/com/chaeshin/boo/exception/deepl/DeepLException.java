package com.chaeshin.boo.exception.deepl;

public class DeepLException extends RuntimeException {
    public DeepLException() {
        super();
    }

    public DeepLException(String message) {
        super(message);
    }

    public DeepLException(String message, Throwable cause) {
        super(message, cause);
    }

    public DeepLException(Throwable cause) {
        super(cause);
    }

    protected DeepLException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
