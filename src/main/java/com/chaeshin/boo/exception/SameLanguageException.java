package com.chaeshin.boo.exception;

/**
 * 리뷰 본문 언어와 번역하고자 하는 언어(즉, 해당 번역을 요청한 사용자의 선호 언어)가 동일할 경우.
 */
public class SameLanguageException extends RuntimeException {
    public SameLanguageException() {
        super();
    }

    public SameLanguageException(String message) {
        super(message);
    }

    public SameLanguageException(String message, Throwable cause) {
        super(message, cause);
    }

    public SameLanguageException(Throwable cause) {
        super(cause);
    }

    protected SameLanguageException(String message, Throwable cause, boolean enableSuppression,
                                    boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
