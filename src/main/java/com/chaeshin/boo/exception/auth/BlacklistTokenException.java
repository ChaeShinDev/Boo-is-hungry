package com.chaeshin.boo.exception.auth;

/**
 * 토큰이 블랙리스트에 존재하는 경우 발생하는 예외
 */
public class BlacklistTokenException extends TokenException {
    public BlacklistTokenException() {
        super();
    }

    public BlacklistTokenException(String message) {
        super(message);
    }

    public BlacklistTokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public BlacklistTokenException(Throwable cause) {
        super(cause);
    }

    protected BlacklistTokenException(String message, Throwable cause, boolean enableSuppression,
                                      boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
