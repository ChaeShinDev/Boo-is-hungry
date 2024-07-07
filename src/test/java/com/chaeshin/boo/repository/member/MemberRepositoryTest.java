package com.chaeshin.boo.repository.member;

import com.chaeshin.boo.domain.Member;
import com.chaeshin.boo.domain.review.Review;
import com.chaeshin.boo.repository.review.ReviewRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
@Transactional
public class MemberRepositoryTest {
    @Autowired MemberRepository memberRepository;
    @Autowired ReviewRepository reviewRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    void 회원_가입_ID_조회(){
        Member member = new Member();

        ReflectionTestUtils.setField(member, "nickname", "test");
        Member created = memberRepository.save(member);

        Optional<Member> found = memberRepository.findById(created.getId()); // Optional<T> : 해당 객체가 null 일지 아닐지 확실하지 않은 경우에 안전하게 객체를 확인하고 처리하기 위한 일종의 Wrapper Class.

        Assertions.assertTrue(found.isPresent()); // True : 영속성 컨텍스트에 해당 Entity 가 존재함.
        Assertions.assertEquals(created.getNickname(), found.get().getNickname());
    }

    @Test
    void 닉네임_회원_조회(){
        Member member = new Member();
        ReflectionTestUtils.setField(member, "nickname", "test");

        Member created = memberRepository.save(member);

        System.out.println(created.getId());

        List<Member> found = memberRepository.findByNickname(created.getNickname());

        for(Member usr : found){
            System.out.println("usr.nickname = " + usr.getNickname());
        }

        Assertions.assertFalse(found.isEmpty());
    }

    @Test
    void 구글_ID_회원_조회(){
        Member member = new Member();
        ReflectionTestUtils.setField(member, "googleId", "blah@google.com");

        Member created = memberRepository.save(member);

        List<Member> found = memberRepository.findByGoogleId(created.getGoogleId());

        for(Member usr : found){
            System.out.println("usr.googleId = " + usr.getGoogleId());
        }

        System.out.println(created.getId());

        Assertions.assertFalse(found.isEmpty());
    }

    @Test
    void 회원_닉네임_변경(){
        Member member = new Member();
        ReflectionTestUtils.setField(member, "nickname", "before");

        Member created = memberRepository.save(member);

        memberRepository.updateNickname(member.getId(), "after");

        Optional<Member> found = memberRepository.findById(created.getId());

        System.out.println(created.getId());

        Assertions.assertTrue(found.isPresent());
        Assertions.assertEquals(found.get().getNickname(), "after");
    }

    @Test
    void 회원_삭제(){
        // given
        Member member = new Member();
        Member created = memberRepository.save(member);

        // when
        memberRepository.delete(created);
        Optional<Member> found = memberRepository.findById(created.getId());

        System.out.println(created.getId());

        // then
        Assertions.assertFalse(found.isPresent());
    }

    @Test
    @Transactional
    void 리뷰_뭉치_가져오기(){

        // given
        Member member = new Member();
        Review rev1 = new Review();
        Review rev2 = new Review();

        rev1.updateMember(member);
        rev2.updateMember(member);

        // when
        Member created = memberRepository.save(member);
        reviewRepository.save(rev1);
        reviewRepository.save(rev2);
        Member found = memberRepository.findById(created.getId()).get();

        // then
        Assertions.assertEquals(2, found.getReviews().size());

    }
}
