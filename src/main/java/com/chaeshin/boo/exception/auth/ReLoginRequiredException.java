package com.chaeshin.boo.exception.auth;

/**
 * 아래의 두 가지 상황으로 인해 재로그인이 필요할 때 발생하는 예외.
 * <br></br>
 * <br></br>
 * 1) Access Token / Refresh Token 모두 만료.
 * <br></br>
 * 2) Request Header 에 첨부된 Refresh Token 이 Blacklist 에 존재하지 않는 경우 발생.
 * <br></br>
 * <br></br>
 * 2)의 경우, Server 쪽으로 전송된 요청과 그 요청의 헤더에 첨부된 refresh token 모두, 누군가가 과거 어떤 시점에 탈취하여 지금 보내는 것일 가능성(MITM) 존재.
 * <br></br>
 * 하지만 Stateless 특성 상 둘 중 어느 쪽인지 판단할 수 있는 데이터가 충분하지 않으므로, 재로그인을 요청하는 것이 가장 안전하다.
 *
 */
public class ReLoginRequiredException extends TokenException {
    public ReLoginRequiredException() {
        super();
    }

    public ReLoginRequiredException(String message) {
        super(message);
    }

    public ReLoginRequiredException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReLoginRequiredException(Throwable cause) {
        super(cause);
    }

    protected ReLoginRequiredException(String message, Throwable cause, boolean enableSuppression,
                                       boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
