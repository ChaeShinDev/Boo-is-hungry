package com.chaeshin.boo.controller.dto.member;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberLoggedInDto {

    // JWT Access Token.
    private String accessToken;

    // JWT Refresh Token.
    private String refreshToken;

    // 기존재 회원 여부.
    private boolean existUser;
}
