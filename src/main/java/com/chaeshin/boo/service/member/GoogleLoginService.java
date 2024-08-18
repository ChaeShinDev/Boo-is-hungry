package com.chaeshin.boo.service.member;

import com.chaeshin.boo.controller.dto.member.GoogleRequestDto;
import com.chaeshin.boo.controller.dto.member.GoogleResponseDto;
import com.chaeshin.boo.controller.dto.member.GoogleUserInfoResponseDto;
import com.chaeshin.boo.controller.dto.member.MemberLoggedInDto;
import com.chaeshin.boo.service.member.auth.JwtAuthService;
import com.chaeshin.boo.service.member.auth.JwtPayload;
import com.chaeshin.boo.service.member.auth.TokenType;
import java.util.HashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleLoginService {

    @Value("${google.appKey}")
    private String clientId;

    @Value("${google.secretKey}")
    private String secretKey;

    private final JwtAuthService jwtAuthService;

    private final MemberService memberService;


    /**
     * 구글 로그인 인터페이스로 연결되는 URL 반환
     * @return
     */
    public String getLoginUrl(){
        String baseUrl = "https://accounts.google.com/o/oauth2/v2/auth?client_id=";
        String redirectUrl = "&redirect_uri=http://localhost:8080/accounts/login/&response_type=code&scope=email%20profile%20openid&access_type=offline";

        return baseUrl + clientId + redirectUrl;
    }

    /**
     * <b><i>클라이언트 단에서 로그인한 User의 정보를 가져오는 Service method.</b></i>
     * <br></br>
     * <br></br>
     * 크게 다음의 두 가지 과정으로 진행된다.
     * <p></p>
     * 1) 302 MOVED PERMANENTLY 와 함께 쿼리 파라미터로 주어진 접근 인가 코드(code)를 GoogleRequestDto에 담아 구글 로그인 API 엔드포인트로 POST 한다.
     * <br></br>
     * <br></br>
     * 2) 해당 POST 요청이 Valid할 경우 돌아오는 Response를 GoogleResponseDto에 bind한다.
     * <br></br>
     * <br></br>
     * 3) 응답으로 돌아오는 Access code를 받아 구글의 사용자 정보 조회 API 엔드포인트로 GET Request 전송 시 유저 정보가 Response로 돌아온다.
     * <br></br>
     * <br></br>
     * 4) 유저 정보를 GoogleUserInfoResponseDto 에 Bind 하여 로그인한 유저 정보 확보.
     * <br></br>
     * <br></br>
     * 5) 확보한 유저 정보 중 e-mail을 탐색키로 사용하여(memberService.findByEmail()) 해당 회원 기존재 여부 확인.
     * <br></br>
     * <br></br>
     * 6) 가입이 필요한 경우(기존재 X) 가입 로직을 호출하여 Hibernate 를 통해 MySQL DB에 유저 정보 저장. (memberService.save(Member member))
     * <br></br>
     * <br></br>
     * 7) 로그인된 회원에게 JWT 토큰 발급 후 이를 MemberLoggedInDto에 담아 웹 계층으로 반환.
     * @param grantCode
     * @return
     */
    public MemberLoggedInDto processGoogleLogin(String grantCode){

        String baseUrl = "https://oauth2.googleapis.com/token";
        String redirectUri = "http://localhost:8080/accounts/login/"; // Access Token 반환하며 Redirect 될 url
        String userInfoUrl = "https://www.googleapis.com/userinfo/v2/me"; // Access Token을 Header 에 담아 보낼 시 유저 정보를 보내주는 구글 API Endpoint

        // 1)
        GoogleRequestDto googleRequestDto = GoogleRequestDto.builder()
                                                            .clientId(clientId)
                                                            .clientSecret(secretKey)
                                                            .grantType("authorization_code")
                                                            .code(grantCode)
                                                            .redirectUri(redirectUri)
                                                            .build();

        WebClient webClientForAccessCode = WebClient.create(baseUrl);
        GoogleResponseDto googleResponseDto = webClientForAccessCode.post()
                .body(BodyInserters.fromValue(googleRequestDto))
                .retrieve()
                .bodyToMono(GoogleResponseDto.class)
                .block();

        // 2)
        WebClient webClientForUserInfo = WebClient.create(userInfoUrl);
        GoogleUserInfoResponseDto googleUserInfoResponseDto = webClientForUserInfo.get()
                .header("Authorization", googleResponseDto.getTokenType() + " " + googleResponseDto.getAccessToken())
                .retrieve()
                .bodyToMono(GoogleUserInfoResponseDto.class)
                .block();

        String googleId = googleUserInfoResponseDto.getId(); // 구글 PK
        boolean exists = memberService.isExist(googleId); // 기존재 유저 여부 판단.

        Long memberId = memberService.join(googleId);

        JwtPayload payload = JwtPayload.builder()
                .memberId(memberId)
                .build();

        HashMap<TokenType, String> tokens = jwtAuthService.provideToken(payload);
        String accessToken = tokens.get(TokenType.ACCESS);
        String refreshToken = tokens.get(TokenType.REFRESH);

        return MemberLoggedInDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .existUser(exists)
                .build();
    }


}
