package com.chaeshin.boo.service.member;

import com.chaeshin.boo.domain.LangCode;
import com.chaeshin.boo.domain.Member;
import com.chaeshin.boo.repository.member.MemberRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;

    /**
     * 회원 가입. Entity 생성 및 PersistenceContext & DB에 저장.
     * @param googleId
     * @return
     */
    @Transactional
    public Long join(String googleId){
        List<Member> memberList = memberRepository.findByGoogleId(googleId);
        Long memberId = null;
        Member savedMember = null;

        if(memberList.isEmpty()){
            Member member = Member.builder()
                    .googleId(googleId)
                    .langCode(LangCode.KO) // 한국어 기본.
                    .build();

            savedMember = memberRepository.save(member);
            memberId = savedMember.getId();
        }
        else {
            savedMember = memberList.get(0); // 이 부분은 나중에 Optional<Member>로 Refactor 하는 게 좋을 것 같다.
            memberId = savedMember.getId();
        }

        return memberId;
    }

    /**
     * 이미 존재하는 Member인지 확인.
     * @param googleId
     * @return
     */
    public boolean isExist(String googleId){
        return !(memberRepository.findByGoogleId(googleId).isEmpty());
    }


    /**
     * ID를 통해 Member 검색.
     * @param memberId
     * @return
     */
    public Optional<Member> getMemberById(Long memberId){
        return memberRepository.findById(memberId);
    }

    /**
     * Member 닉네임 변경
     * @param memberId
     * @param newNickname
     */
    @Transactional
    public String updateNickname(Long memberId, String newNickname){
        return memberRepository.updateNickname(memberId, newNickname);
    }

    /**
     * 회원 탈퇴.
     * <br></br>
     * <br></br>
     * - 해당하는 Member가 존재하지 않을 시 IllegalArgumentException 발생.
     * @param memberId
     */
    @Transactional
    public void deleteMember(Long memberId){
        memberRepository.deleteById(memberId);
    }

}
