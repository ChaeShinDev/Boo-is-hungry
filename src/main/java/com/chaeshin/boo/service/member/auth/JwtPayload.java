package com.chaeshin.boo.service.member.auth;

import java.util.Date;
import lombok.Builder;
import lombok.Data;

/**
 * JWT 생성 및 발급을 위한 정보를 담아 Controller - JwtAuthService 사이를 오가게 될 DTO.
 */
@Data
@Builder
public class JwtPayload {

    private Long memberId;
    private Date issuedDate;

    public void setIssuedDate(){
        this.issuedDate = new Date();
    }
}
