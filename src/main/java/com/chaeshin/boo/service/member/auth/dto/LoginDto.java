package com.chaeshin.boo.service.member.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LoginDto {
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("refresh_token")
    private String refreshToken;
    @JsonProperty("exist_user")
    private boolean existUser;

    public LoginDto(String accessToken, String refreshToken, boolean existUser) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.existUser = existUser;
    }
}
