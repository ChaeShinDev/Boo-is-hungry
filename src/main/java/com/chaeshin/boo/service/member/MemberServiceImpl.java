package com.chaeshin.boo.service.member;

import com.chaeshin.boo.domain.Member;
import com.chaeshin.boo.repository.member.MemberRepository;
import com.chaeshin.boo.service.member.dto.MemberInfoDto;
import com.chaeshin.boo.utils.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;


    @Override
    @Transactional
    public ResponseDto updateNickname(Long memberId, String nickname) {
        Member member = memberRepository.findById(memberId).get();
        member.updateMemberNickname(nickname);
        return new ResponseDto("닉네임 설정 성공", null);
    }

    @Override
    public ResponseDto<MemberInfoDto> getMemberInfo(Long memberId) {
        Member member = memberRepository.findById(memberId).get();
        MemberInfoDto memberInfoDto = new MemberInfoDto(member);
        return new ResponseDto("유저 정보 반환 성공", memberInfoDto);
    }
}
