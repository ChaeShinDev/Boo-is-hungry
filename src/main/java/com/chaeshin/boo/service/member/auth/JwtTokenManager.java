package com.chaeshin.boo.service.member.auth;

import com.chaeshin.boo.exception.auth.BlacklistTokenException;
import com.chaeshin.boo.exception.auth.EmptyTokenException;
import com.chaeshin.boo.exception.auth.ExpiredAccessTokenException;
import com.chaeshin.boo.exception.auth.IllegalTokenException;
import com.chaeshin.boo.exception.auth.ReLoginRequiredException;
import com.chaeshin.boo.exception.auth.TokenException;
import com.chaeshin.boo.exception.auth.WrongTokenTypeException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jwts.SIG;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


/**
 * JWT 를 생성, 발급, 검증하는 Component class. auth 패키지 내에서만 사용되기 때문에 package-private 설정.
 */
// @Component // Bean 에는 Static 넣지 않기.

@Service
class JwtTokenManager {

    @Value("${jjwt.secretKey}")
    private String initSecretKey;

    private SecretKey secretKey; // HMAC-SHA256 알고리즘을 활용.
    private HashMap<Long, String> accessTokenBlacklist = new HashMap<>(); // 로그아웃한 유저의 액세스 토큰
    private HashMap<Long, String> refreshTokenBlacklist = new HashMap<>(); // 로그아웃한 유저의 리프레시 토큰

    @Value("${spring.application.name}")
    private String issuer;

    @Value("${jjwt.accessExpire}")
    private Long accessExpireAfter; // Access Token Validity Duration. Time Unit - Millisecond

    @Value("${jjwt.refreshExpire}")
    private Long refreshExpireAfter; // Expiration Token Validity Duration. Time Unit - Millisecond

    /**
     * 초기화 콜백 메서드 : Secret Key 주입
     * @param secretKey
     */
    @PostConstruct
    private void setSecretKey(){
        // secret key 로 SecretKey 타입이 아닌 (즉, 암호화를 거치지 않은 public 상태인 key)를 사용할 경우,
        // JJWT 는 InvalidKeyException 발생시킨다.
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(initSecretKey));
    }


    /**
     * JWT 생성 메서드.
     * <p></p>
     * JWT는 세 부분으로 구성된다. 우리 서비스의 JWT는 각 부분에 다음과 같은 정보를 담는다.
     * <br></br>
     * <br></br>
     * 1) Header : Token의 타입(JWT) & Hashing Algorithm. 우리는 JJWT의 자동 헤더 생성 및 제공 기능을 활용한다.
     * <br></br>
     * 2) Payload : JWT에 담을 정보가 보관되는 부분. Registered / Public / Private 세 가지 종류의 Claim(정보의 조각)으로 구성.
     * <br></br>
     * 3) Signature : 서명. Header와 Payload의 데이터의 Base64 인코딩 값을 합친 후 사전에 설정된 secretKey를 활용해 Hashing하여 생성한다.
     * <br></br>
     * @param payload
     * @param tokenType
     * @return token
     */
    String provide(JwtPayload payload, TokenType tokenType){
        // TokenType 에 부합하는 유효 기간 설정.
        Long duration = (tokenType.equals(TokenType.ACCESS)) ? (accessExpireAfter) : (refreshExpireAfter);

        // JwtPayload.setDate() 통해 토큰 생성 시점 확정 및 해당 필드 초기화.
        payload.setIssuedDate();

        // 토큰 생성 : 이때 payload 생성 시점이 같을 수 없기 때문에 중복되는 토큰이 있는지 확인할 필요가 없다(?).
        return Jwts.builder()
                .header().type("JWT") // JWT Organization 공식 사이트에서 제시하는 방향.
                .and()
                .issuer(issuer)
                .id(UUID.randomUUID().toString())
                .issuedAt(payload.getIssuedDate())
                .claim("token_type", String.valueOf(tokenType)) // ENUM 나갈 때 형태를 확정하는 방식을 마련하지 않으면, ENUM 이 오가는 형태 일관성 예측 어려움.
                .expiration(new Date(payload.getIssuedDate().getTime() + duration))
                .subject(payload.getMemberId().toString())
                .signWith(secretKey, SIG.HS512) // .signWith() : Produces "JWS(Jason Web Signature)"
                .compact();
    }

    /**
     * Refresh Token을 받아 parse 후 해당 정보를 이용해 새로운 토큰 재발급.
     * <br></br>
     * <br></br>
     * 기존 refresh token은 blacklist에 추가한다.
     * @param refreshToken
     * @return
     */
    HashMap<TokenType, String> reissue(String refreshToken){

        // Refresh token 재검증.
        verifyRefreshToken(refreshToken);

        // Refresh token parsing.
        Jws<Claims> parsedRefreshToken = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(refreshToken);
        Claims parsedClaims = parsedRefreshToken.getPayload();

        // 발급 대상(subject) memberId 확보
        Long memberId = Long.parseLong(parsedClaims.getSubject());

        // Refresh token 블랙리스트에 추가.
        addBlacklist(refreshToken, memberId, TokenType.REFRESH);

        // 확보한 ID를 이용해 토큰 생성에 필요한 데이터를 담은 객체인 JwtPayload 객체 생성.
        JwtPayload payload = JwtPayload.builder()
                .memberId(memberId)
                .build();
        // payload 객체 필드인 issuedDate(발급 시간) 현재로 설정.
        payload.setIssuedDate();

        String newAccessToken = provide(payload, TokenType.ACCESS);
        String newRefreshToken = provide(payload, TokenType.REFRESH);

        HashMap<TokenType, String> regeneratedTokens = new HashMap<>();
        regeneratedTokens.put(TokenType.ACCESS, newAccessToken);
        regeneratedTokens.put(TokenType.REFRESH, newRefreshToken);

        return regeneratedTokens;
    }

    /**
     * Access token 유효성을 검증하는 메서드.
     * <p></p>
     * Access token 유효성은 두 가지 요인으로 결정된다.
     * <br></br>
     * <br></br>
     * 1) 토큰 블랙리스트 유무 : 블랙리스트에 들어간 토큰이라면, 해당 요청 자체가 처리되지 않도록 한다. : HTTP 401 반환.
     * <br></br>
     * 2) 토큰 유효기간 : 블랙리스트 토큰이 아니라면, 확인 시점에 해당 토큰의 유효 기간이 지나지 않았는가? 토큰 생성 시점이 현 시간 보다 뒤(Premature)하진 않은가?
     * <br></br>
     * <br></br>
     * <b><i>발생 Exception 별 Handling.</i></b>
     * <br></br>
     * <br></br>
     * 1) IllegalToken / EmptyToken / ReissueRequired / ReLoginRequired / InvalidToken : 401 Unauthorized.
     * <br></br>
     * 2) ExpiredToken :
     * <br></br>
     *
     * @param accessToken
     * @return
     */
    boolean verifyAccessToken(String accessToken) throws TokenException {
        try {
            Jws<Claims> parsedToken = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(accessToken); // 유효한 토큰이 아닌 경우(복호화 불가할 경우) IllegalArgumentException 발생.

            Claims parsedClaims = parsedToken.getPayload(); // Parse payload of given access token.
            TokenType tokenType = TokenType.valueOf(parsedClaims.get("token_type", String.class)); // Token Type
            Long memberId = Long.parseLong(parsedClaims.getSubject()); // member PK

            // Param으로 전달된 토큰의 타입이 정확한지 검증.
            if(tokenType != TokenType.ACCESS){throw new WrongTokenTypeException("토큰 타입이 올바르지 않습니다.");}

            // 해당 토큰의 블랙리스트 내 존재 여부 검증.
            String tokenFromBlacklist = accessTokenBlacklist.get(memberId);
            if(tokenFromBlacklist != null && tokenFromBlacklist.equals(accessToken)){throw new BlacklistTokenException("로그아웃하여 블랙리스트에 등록된 토큰입니다.");} // 401

            // 해당 토큰의 발급 날짜 & 만료 날짜 유효성 검증
            if(parsedClaims.getIssuedAt().after(new Date())){throw new IllegalTokenException("토큰 생성 시점에 문제가 있습니다.");}
            if(parsedClaims.getExpiration().before(new Date())){throw new ExpiredAccessTokenException("액세스 토큰 만료.");}

            return true;

        } catch (IllegalArgumentException iae){
            iae.printStackTrace();
            throw new EmptyTokenException("토큰 내 데이터가 존재하지 않습니다");
        } catch (JwtException je){
            je.printStackTrace();
            throw new IllegalTokenException("토큰 형태가 올바르지 않습니다.");
        }
    }

    /**
     * Refresh 토큰 검증.
     * @param refreshToken
     * @return
     */
    boolean verifyRefreshToken(String refreshToken){
        try{
            Jws<Claims> parsedToken = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(refreshToken);

            Claims parsedClaims = parsedToken.getPayload();
            Long memberId = Long.parseLong(parsedClaims.getSubject());
            TokenType tokenType = TokenType.valueOf(parsedClaims.get("token_type", String.class));

            // Param으로 전달된 토큰의 타입이 정확한지 검증.
            if(tokenType != TokenType.REFRESH){throw new WrongTokenTypeException("토큰 타입이 올바르지 않습니다.");}

            // 해당 토큰의 블랙리스트 내 존재 여부 검증.
            String tokenFromBlacklist = refreshTokenBlacklist.get(memberId);
            if(tokenFromBlacklist.equals(refreshToken)){throw new BlacklistTokenException("로그아웃하여 블랙리스트에 등록된 토큰입니다.");} // 401

            // 해당 토큰의 발급 날짜 & 만료 날짜 유효성 검증
            if(parsedClaims.getIssuedAt().after(new Date())){throw new IllegalTokenException("토큰 생성 시점에 문제가 있습니다.");}
            if(parsedClaims.getExpiration().before(new Date())){throw new ReLoginRequiredException("리프레시 토큰 만료.");}

            return true; // Scenario which tokens have to be reissued.

        } catch (IllegalArgumentException iae){
            iae.printStackTrace();
            throw new EmptyTokenException("토큰 내 데이터가 존재하지 않습니다");
        } catch (JwtException je){
            je.printStackTrace();
            throw new IllegalTokenException("토큰 형태가 올바르지 않습니다.");
        }
    }

    /**
     * Client로부터 전달받은 Refresh Token 을 블랙리스트에 등록.
     * @param token
     */
    void addBlacklist(String token){
        Jws<Claims> parsed = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token);

        Claims claims = parsed.getPayload();
        Long memberId = Long.parseLong(claims.getSubject());
        TokenType tokenType = TokenType.valueOf(claims.get("token_type", String.class));

        if(tokenType.equals(TokenType.ACCESS)){accessTokenBlacklist.put(memberId, token);}
        else{refreshTokenBlacklist.put(memberId, token);}
    }

    /**
     * 불필요한 Parsing 작업 반복을 막기 위해 따로 구현.
     * @param token
     * @param memberId
     * @param tokenType
     */
    void addBlacklist(String token, Long memberId, TokenType tokenType){
        if(tokenType.equals(TokenType.ACCESS)){accessTokenBlacklist.put(memberId, token);}
        else{refreshTokenBlacklist.put(memberId, token);}
    }


    Long parseMemberIdFromToken(String token) throws TokenException, NumberFormatException {
        try{
            Jws<Claims> parsed = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);

            Claims parsedClaims = parsed.getPayload();

            return Long.parseLong(parsedClaims.getSubject());

        } catch (IllegalArgumentException iae){
            iae.printStackTrace();
            throw new EmptyTokenException("토큰 내 데이터가 존재하지 않습니다");
        } catch (JwtException je){
            je.printStackTrace();
            throw new IllegalTokenException("토큰 형태가 올바르지 않습니다.");
        }
    }


}
