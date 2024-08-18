package com.chaeshin.boo.controller.dto.member;

import com.chaeshin.boo.domain.LangCode;
import lombok.Builder;
import lombok.Data;

/**
 * Review / ReviewImage 와 같은 다른 엔티티를 직렬화 하는 과정에서 해당 엔티티의 Member 필드 데이터 직렬화에 사용될 DTO.
 */
@Data
@Builder
public class MemberEmbeddedDto {

    private Long memberId;
    private String nickname;
    private LangCode langCode;


}
