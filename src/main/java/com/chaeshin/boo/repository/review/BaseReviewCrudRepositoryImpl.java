package com.chaeshin.boo.repository.review;

import com.chaeshin.boo.domain.review.Review;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class BaseReviewCrudRepositoryImpl implements BaseReviewCrudRepository {

    @PersistenceContext EntityManager em;

    /**
     * 회원 ID 로 리뷰 조회.
     * <br></br>
     * Q. 'Pageable' 도입하는 것이 좋지 않을까?
     * @param userId
     * @return
     */
    @Override
    public List<Review> findAllByUserId(Long userId) {
        return em.createQuery("select r from Review r where r.user.id = :userId", Review.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    /**
     * 식당 ID 로 리뷰 조회
     * @param restaurantId
     * @return
     */
    @Override
    public List<Review> findAllByRestaurantId(Long restaurantId) {
        return em.createQuery("select r from Review r where r.restaurant.id = :restaurantId", Review.class)
                .setParameter("restaurantId", restaurantId)
                .getResultList();
    }
}
