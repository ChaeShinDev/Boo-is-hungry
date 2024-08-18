package com.chaeshin.boo.controller.dto.member;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GoogleResponseDto {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("expires_in")
    private int expiresIn;

    @JsonProperty("scope")
    private String scope;

    @JsonProperty("token_type")
    private String tokenType; // Bearer 고정.

    @JsonProperty("id_token")
    private String idToken; // Refresh Token
}
