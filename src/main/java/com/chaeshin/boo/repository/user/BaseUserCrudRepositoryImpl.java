package com.chaeshin.boo.repository.user;

import com.chaeshin.boo.domain.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.util.ReflectionUtils;

/**
 * BaseUserCrudRepository 구현체.
 */

@Repository // 사용자 정의 Repository interface 의 구현체 이름 뒤에 'Impl' + @Repository -> Spring Data JPA 가 자동 인식.
public class BaseUserCrudRepositoryImpl implements BaseUserCrudRepository {

    @PersistenceContext
    private EntityManager em;

    /**
     * 회원 닉네임으로 조회.
     * @param nickname
     * @return List of User
     */
    @Override
    public List<User> findByNickname(String nickname) {
        return em.createQuery("select u from User u where u.nickname = :nickname", User.class)
                .setParameter("nickname", nickname)
                .getResultList();
    }

    /**
     * 회원 Google ID 로 조회
     * @param googleId
     * @return List of User
     */
    @Override
    public List<User> findByGoogleId(String googleId) {
        return em.createQuery("select u from User u where u.googleId = :googleId", User.class)
                .setParameter("googleId", googleId)
                .getResultList();
    }

    @Override
    public void updateNickname(Long id, String nickname) {
        User user = em.find(User.class, id); // Q. 예외 처리를 어떻게 할 것인가?
        user.updateUserNickname(nickname); // Dirty Check 와 Domain 편의 기능을 함께 활용한 변경 Query method.
    }
}
