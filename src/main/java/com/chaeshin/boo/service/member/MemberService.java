package com.chaeshin.boo.service.member;

import com.chaeshin.boo.service.member.dto.MemberInfoDto;
import com.chaeshin.boo.utils.ResponseDto;

public interface MemberService {

    /*유저 닉네임 설정*/
    ResponseDto updateNickname(Long memberId, String nickname);

    /*유저 정보 조회*/
    ResponseDto<MemberInfoDto> getMemberInfo(Long memberId);
}
