package com.chaeshin.boo.service.member.auth;

import com.chaeshin.boo.exception.auth.TokenException;
import java.util.HashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * JWT 토큰 발행, 갱신 및 폐기 로직을 담고있는 Service.
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class JwtAuthService {

    private final JwtTokenManager jwtTokenManager;


    /**
     * 토큰 생성 및 발급.
     * @param payload
     * @return
     */
    public HashMap<TokenType, String> provideToken(JwtPayload payload){

        // 토큰 생성
        String accessToken = jwtTokenManager.provide(payload, TokenType.ACCESS);
        String refreshToken = jwtTokenManager.provide(payload, TokenType.REFRESH);

        // 토큰 반환
        HashMap<TokenType, String> tokens = new HashMap<>();
        tokens.put(TokenType.ACCESS, accessToken);
        tokens.put(TokenType.REFRESH, refreshToken);

        return tokens;
    }


    /**
     * 액세스 토큰의 유효성 검증 결과를 확인하는 메서드. 유효하지 않은 경우 Exception 을 발생시킨다.
     * @param accessToken
     * @return
     */
    public boolean verifyAccessToken(String accessToken) throws TokenException {
        return jwtTokenManager.verifyAccessToken(accessToken);
    }

    public boolean verifyRefreshToken(String refreshToken) throws TokenException {
        return jwtTokenManager.verifyRefreshToken(refreshToken);
    }

    /**
     * Access Token 은 만료되었지만 Refresh Token 은 만료되지 않은 경우에도 Access Token과 Refresh Token 모두 재발급한다.
     * <br></br>
     * <br></br>
     * 이때 두 가지 토큰을 모두 다 재발급하는 이유는 <i>OAuth</i> 에서 제시하는 <b><i>Refresh Token Rotation</i></b> 준수 위함.
     */
    public HashMap<TokenType, String> reissueToken(String refreshToken){
        return jwtTokenManager.reissue(refreshToken);
    }

    /**
     * 요청 헤더의 "Authorization" 에서 토큰 파싱.
     * @param auth
     * @return
     */
    public String parseToken(String auth){
        // String[] parsed = auth.split(" ");
        // log.info("parsed: " + parsed[1]);
        // return parsed[1];
        if (auth != null && auth.startsWith("Bearer ")) {
            return auth.substring(7);
        }
        return null;
    }


    /**
     * 토큰으로부터 memberId parse & return.
     * @param token
     * @return
     */
    public Long getMemberIdFromToken(String token) {
        return jwtTokenManager.parseMemberIdFromToken(token);
    }
}
