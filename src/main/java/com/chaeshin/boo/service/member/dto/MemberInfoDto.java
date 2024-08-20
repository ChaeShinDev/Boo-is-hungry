package com.chaeshin.boo.service.member.dto;

import com.chaeshin.boo.domain.LangCode;
import com.chaeshin.boo.domain.Member;
import lombok.Data;

@Data
public class MemberInfoDto {

    private Long id;
    private String nickname;
    private LangCode language;

    public MemberInfoDto(Member member) {
        this.id = member.getId();
        this.nickname = member.getNickname();
        this.language = member.getLangCode();
    }
}
