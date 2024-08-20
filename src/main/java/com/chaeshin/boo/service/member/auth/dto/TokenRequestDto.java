package com.chaeshin.boo.service.member.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TokenRequestDto {

    @JsonProperty("client_id")
    private final String clientId;
    @JsonProperty("client_secret")
    private final String clientSecret;
    @JsonProperty("grant_type")
    private final String grantType = "authorization_code";
    @JsonProperty("redirect_uri")
    private String redirectUri;
    private String code;

    public TokenRequestDto(String clientId, String clientSecret) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    public void setCodeAndRedirectUri(String code, String redirectUri) {
        this.code = code;
        this.redirectUri = redirectUri;
    }
}
