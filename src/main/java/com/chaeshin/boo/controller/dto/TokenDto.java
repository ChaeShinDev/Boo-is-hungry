package com.chaeshin.boo.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenDto {

    @JsonProperty("access")
    private String accessToken;

    @JsonProperty("refresh")
    private String refreshToken;
}
