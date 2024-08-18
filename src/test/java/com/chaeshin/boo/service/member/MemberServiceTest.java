package com.chaeshin.boo.service.member;

import static org.junit.jupiter.api.Assertions.*;

import com.chaeshin.boo.domain.LangCode;
import com.chaeshin.boo.domain.Member;
import com.chaeshin.boo.repository.member.MemberRepository;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    static final Member member = new Member("johndoe@gmailc.com", "nickname", LangCode.KO);

    @Test
    @DisplayName("회원 가입 테스트")
    void join() {
        // when
        Long memberId = memberService.join(member.getGoogleId()); // Member 생성 및 저장
        Optional<Member> foundMember = memberService.getMemberById(memberId); // 멤버 조회.

        // then
        Assertions.assertTrue(foundMember.isPresent()); // Member 존재 여부 평가.
        Assertions.assertEquals(member.getGoogleId(), foundMember.get().getGoogleId()); // GoogleId 일치 여부 평가.
    }

    @Test
    @DisplayName("회원 존재 여부 테스트")
    void isExist() {
        // given
        Long memberId = memberService.join(member.getGoogleId()); // 회원 가입

        // when
        Optional<Member> foundMember = memberService.getMemberById(memberId); // 회원 조회

        // then
        Assertions.assertTrue(foundMember.isPresent()); // 해당 회원 정상 조회 여부 검증
        Assertions.assertTrue(memberService.isExist(member.getGoogleId())); //
    }

    @Test
    @DisplayName("회원 조회 테스트")
    void getMemberById() {
        // given
        Long memberId = memberService.join(member.getGoogleId()); // Member 생성 및 저장

        // when
        Optional<Member> foundMember = memberService.getMemberById(memberId); // 멤버 조회.

        // then
        Assertions.assertTrue(foundMember.isPresent()); // Member 존재 여부 평가.

    }

    @Test
    @DisplayName("닉네임 변경 테스트")
    void updateNickname() {
        // given
        Long memberId = memberService.join(member.getGoogleId()); // Member 생성 및 저장
        String newNickname = "newNick!";

        // when
        String updatedNickname = memberService.updateNickname(memberId, newNickname); // 닉네임 변경

        // then
        Assertions.assertEquals(newNickname, updatedNickname); // 닉네임 변경 여부 평가.
    }

    @Test
    void deleteMember() {
        // given
        Long memberId = memberService.join(member.getGoogleId()); // Member 생성 및 저장

        // when
        memberService.deleteMember(memberId); // 회원 삭제

        // then
        Assertions.assertFalse(memberService.isExist(member.getGoogleId())); // 회원 삭제 여부 평가.
    }
}