package com.chaeshin.boo.controller.member;

import com.chaeshin.boo.controller.dto.BaseResponseDto;
import com.chaeshin.boo.controller.dto.DataResponseDto;
import com.chaeshin.boo.controller.dto.member.MemberInfoDto;
import com.chaeshin.boo.controller.dto.member.MemberLoggedInDto;
import com.chaeshin.boo.controller.dto.TokenDto;
import com.chaeshin.boo.domain.Member;
import com.chaeshin.boo.exception.auth.TokenException;
import com.chaeshin.boo.service.member.GoogleLoginService;
import com.chaeshin.boo.service.member.MemberService;
import com.chaeshin.boo.service.member.auth.JwtAuthService;
import com.chaeshin.boo.service.member.auth.TokenType;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final GoogleLoginService googleLoginService;
    private final JwtAuthService jwtAuthService;

    /**
     * 구글 로그인 프로세스의 가장 첫 단계.
     * <p></p>
     * - <b>구글 로그인 API Provider가 제공하는 로그인 서비스 Endpoint URL을 Client에게 전달한다.</b>
     *
     * @return
     */
    @GetMapping("accounts/google/")
    public String googleLoginInitiate() {
        return googleLoginService.getLoginUrl();
    }

    /**
     * 구글 로그인 두 번째이자 마지막 단계.
     * <br></br>
     * <br></br>
     * - 이 API endpoint는 Client 쪽에서 직접 접근하는 것이 아닌, 구글이 로그인 처리 완료 후 redirect 하는 endpoint 이다.
     * <br></br>
     * <br></br>
     * @param grantCode
     * @return
     */
    @GetMapping("accounts/login/")
    public ResponseEntity<DataResponseDto<MemberLoggedInDto>> googleLoginProcess(@RequestParam("code") String grantCode) {
        MemberLoggedInDto grantedMemberLoggedInDto = googleLoginService.processGoogleLogin(grantCode);

        String msg = null;

        if(grantedMemberLoggedInDto.isExistUser()){
            msg = "기존 사용자 로그인 성공";
            return ResponseEntity.ok().body(new DataResponseDto<>(msg, grantedMemberLoggedInDto));
        }
        else {
            msg = "새로운 사용자 로그인 성공";
            return ResponseEntity.status(HttpStatus.CREATED).body(new DataResponseDto<>(msg, grantedMemberLoggedInDto));
        }
    }

    /**
     * 유저 정보 조회.
     * @param memberId
     * @return
     */
    @GetMapping("accounts/info/{user_id}")
    public ResponseEntity<BaseResponseDto> getMemberInfo(@PathVariable("user_id") @Valid Long memberId){
        Optional<Member> memberOptional = memberService.getMemberById(memberId);
        String msg = null;
        if(memberOptional.isPresent()){
            msg = "유저 정보 반환 성공";
            MemberInfoDto memberInfoDto = MemberInfoDto.builder()
                    .id(memberId)
                    .nickname(memberOptional.get().getNickname())
                    .langCode(memberOptional.get().getLangCode())
                    .build();
            return ResponseEntity.ok().body(new DataResponseDto<MemberInfoDto>(msg, memberInfoDto));
        }
        else {
            msg = "해당 유저가 존재하지 않습니다.";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BaseResponseDto(msg));
        }
    }

    @PostMapping("accounts/nickname/")
    public ResponseEntity<BaseResponseDto> updateNickname(@RequestBody String nickname,
                                                          @RequestHeader("Authorization") String auth){
        try{
            String accessToken = jwtAuthService.parseToken(auth); // Parse access token from header.

            jwtAuthService.verifyAccessToken(accessToken); // Verify the access token.
            Long memberId = jwtAuthService.getMemberIdFromToken(accessToken);

            String updatedNickname = memberService.updateNickname(memberId, nickname);

            if(nickname.equals(updatedNickname)){
                return ResponseEntity.ok(new BaseResponseDto("닉네임 설정 성공"));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponseDto("닉네임 변경 실패."));
            }
        }
        catch (TokenException | NumberFormatException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new BaseResponseDto("Something went wrong with token~"));
        }
    }

    /**
     * Refresh Token 이용해 토큰 재발급.
     * @param refreshToken
     * @return
     */
    @PostMapping("accounts/token/refresh/")
    public ResponseEntity<TokenDto> reissueToken(@RequestBody String auth){

        try{
            String refreshToken = jwtAuthService.parseToken(auth);

            // 토큰 검증 결과 재발급 문제 없는 리프레시 토큰인 경우.
            if(jwtAuthService.verifyRefreshToken(refreshToken)) {
                HashMap<TokenType, String> tokens = jwtAuthService.reissueToken(refreshToken);
                TokenDto tokenDto = TokenDto.builder()
                        .accessToken(tokens.get(TokenType.ACCESS))
                        .refreshToken(tokens.get(TokenType.REFRESH))
                        .build();

                return ResponseEntity.ok(tokenDto);
            }
            // 토큰 검증 결과 문제 있는 리프레시 토큰인 경우 : 만료, 블랙리스트 등재 등.
            else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(TokenDto.builder().build());
            }

        } catch (TokenException e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(TokenDto.builder().build());
        }
    }




}

