package com.chaeshin.boo.service.member.auth;

import static org.junit.jupiter.api.Assertions.*;

import com.chaeshin.boo.config.S3Config;
import com.chaeshin.boo.domain.LangCode;
import com.chaeshin.boo.domain.Member;
import com.chaeshin.boo.service.member.MemberService;
import java.util.Date;
import java.util.HashMap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

/**
 * 토큰 서비스 테스트
 */

@SpringBootTest
class JwtAuthServiceTest {

    @Autowired
    JwtAuthService jwtAuthService;

    @Autowired
    MemberService memberService;

    static final Member member = new Member("johndoe@gmailc.com", "nickname", LangCode.KO);

    @Test
    @DisplayName("토큰 생성 및 발급 테스트")
    void provideToken() {

        // given
        Long memberId = memberService.join(member.getGoogleId());


        // 생성된 Member 정보 담은 JwtPayload 생성.
        JwtPayload memberPayload = JwtPayload.builder().memberId(memberId)
                .issuedDate(new Date())
                .build();

        // when
        HashMap<TokenType, String> tokenMap = jwtAuthService.provideToken(memberPayload);

        // then
        Assertions.assertTrue(tokenMap.containsKey(TokenType.ACCESS));
        Assertions.assertTrue(tokenMap.containsKey(TokenType.REFRESH));
    }

    @Test
    @DisplayName("토큰 파싱 테스트")
    void parseToken() {
        // Given
        String accessToken = "accessToken";
        String auth = "Bearer " + accessToken;

        // when
        String token = jwtAuthService.parseToken(auth);

        // then
        Assertions.assertEquals(accessToken, token);
    }

    @Test
    @DisplayName("토큰에서 멤버 아이디 추출 테스트")
    void getMemberIdFromToken() {

        // given
        Long memberId = memberService.join(member.getGoogleId()); // 회원 가입
        JwtPayload payload = JwtPayload.builder().memberId(memberId).issuedDate(new Date()).build(); // 토큰 생성 위한 JwtPayload 생성

        // when
        HashMap<TokenType, String> tokenMap = jwtAuthService.provideToken(payload); // 토큰 생성
        String accessToken = tokenMap.get(TokenType.ACCESS); // 액세스 토큰 추출
        Long extractedMemberId = jwtAuthService.getMemberIdFromToken(accessToken); // 토큰으로부터 멤버 아이디 추출

        // then
        Assertions.assertEquals(memberId, extractedMemberId); // 멤버 아이디 일치 여부 검증
    }

    @Test
    @DisplayName("액세스 토큰 유효성 검증 테스트")
    void verifyAccessToken() {
        // given
        Long memberId = memberService.join(member.getGoogleId()); // 회원 가입
        JwtPayload payload = JwtPayload.builder().memberId(memberId).issuedDate(new Date()).build(); // 토큰 생성 위한 JwtPayload 생성

        // when
        HashMap<TokenType, String> tokenMap = jwtAuthService.provideToken(payload); // 토큰 생성
        String accessToken = tokenMap.get(TokenType.ACCESS); // 액세스 토큰 추출
        boolean isValid = jwtAuthService.verifyAccessToken(accessToken); // 액세스 토큰 유효성 검증

        // then
        Assertions.assertTrue(isValid); // 액세스 토큰 유효성 검증 결과 검증
    }
}