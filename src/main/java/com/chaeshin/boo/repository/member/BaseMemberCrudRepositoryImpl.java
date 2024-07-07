package com.chaeshin.boo.repository.member;

import com.chaeshin.boo.domain.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * BaseUserCrudRepository 구현체.
 */

@Repository // 사용자 정의 Repository interface 의 구현체 이름 뒤에 'Impl' + @Repository -> Spring Data JPA 가 자동 인식.
public class BaseMemberCrudRepositoryImpl implements BaseMemberCrudRepository {

    @PersistenceContext
    private EntityManager em;

    /**
     * 회원 닉네임으로 조회.
     * @param nickname
     * @return List of Member
     */
    @Override
    public List<Member> findByNickname(String nickname) {
        return em.createQuery("select m from Member m where m.nickname = :nickname", Member.class)
                .setParameter("nickname", nickname)
                .getResultList();
    }

    /**
     * 회원 Google ID 로 조회
     * @param googleId
     * @return List of Member
     */
    @Override
    public List<Member> findByGoogleId(String googleId) {
        return em.createQuery("select m from Member m where m.googleId = :googleId", Member.class)
                .setParameter("googleId", googleId)
                .getResultList();
    }

    @Override
    public void updateNickname(Long id, String nickname) {
        Member member = em.find(Member.class, id); // Q. 예외 처리를 어떻게 할 것인가?
        member.updateMemberNickname(nickname); // Dirty Check 와 Domain 편의 기능을 함께 활용한 변경 Query method.
    }
}
