package com.chaeshin.boo.controller.dto.member;

import com.chaeshin.boo.domain.LangCode;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberInfoDto {
    private Long id; // Member PK
    private String nickname;
    private LangCode langCode;

}
