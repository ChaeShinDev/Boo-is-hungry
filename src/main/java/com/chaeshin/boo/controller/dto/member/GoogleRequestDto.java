package com.chaeshin.boo.controller.dto.member;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;


/**
 * Google의 OAuth2 엔드포인트로부터 Access Token을 발급받기 위해 보내는 RequestDto.
 */

@Data
@Builder
public class GoogleRequestDto {

    @JsonProperty("code")
    private String code;

    @JsonProperty("client_id")
    private String clientId;

    @JsonProperty("client_secret")
    private String clientSecret;

    @JsonProperty("redirect_uri")
    private String redirectUri;

    @JsonProperty("grant_type")
    private String grantType;


}
