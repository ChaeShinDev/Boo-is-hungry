package com.chaeshin.boo.utils.jwt;

import lombok.Data;

@Data
public class TokenReissueDto {

    private String access;
    private String refresh;

    public TokenReissueDto(String access, String refresh) {
        this.access = access;
        this.refresh = refresh;
    }
}
