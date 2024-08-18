package com.chaeshin.boo.exception.deepl;

public class QuotaExceededException extends DeepLException{
    public QuotaExceededException() {
        super();
    }

    public QuotaExceededException(String message) {
        super(message);
    }

    public QuotaExceededException(String message, Throwable cause) {
        super(message, cause);
    }

    public QuotaExceededException(Throwable cause) {
        super(cause);
    }

    protected QuotaExceededException(String message, Throwable cause, boolean enableSuppression,
                                     boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
