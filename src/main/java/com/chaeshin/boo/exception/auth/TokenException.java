package com.chaeshin.boo.exception.auth;

/**
 * 토큰 관련 Exception 의 최상위 부모 클래스
 * <p></p>
 * 이를 상속하는 Token 관련 Exceptions 는 다음과 같다.
 * <br></br>
 * <br></br>
 * <br></br>
 * 1) <b><i>BlacklistTokenException</b></i> : 해당 토큰이 발급 토큰 목록에 존재하지 않거나, 토큰 데이터가 유효하지 않은 경우 발생.
 * <br></br>
 * <br></br>
 * 2) <b><i>ExpiredAccessTokenException</b></i> : Access Token 이 만료되었으나 Refresh Token 은 유효한 경우 발생.
 * <br></br>
 * <br></br>
 */
public class TokenException extends RuntimeException {
    public TokenException() {
        super();
    }

    public TokenException(String message) {
        super(message);
    }

    public TokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public TokenException(Throwable cause) {
        super(cause);
    }

    protected TokenException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
