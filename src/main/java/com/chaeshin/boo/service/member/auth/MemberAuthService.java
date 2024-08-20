package com.chaeshin.boo.service.member.auth;

import com.chaeshin.boo.service.member.auth.dto.LoginDto;
import com.chaeshin.boo.utils.jwt.TokenReissueDto;
import com.chaeshin.boo.utils.ResponseDto;

public interface MemberAuthService {

    /*리다이렉트 URI 리턴*/
    String getRedirectUri(Redirection redirection);

    /*유저 로그인*/
    ResponseDto<LoginDto> login(String code, Redirection redirection);

    /*액세스 토큰 리프레시*/
    TokenReissueDto reIssueAccessToken(String refreshToken);
}
