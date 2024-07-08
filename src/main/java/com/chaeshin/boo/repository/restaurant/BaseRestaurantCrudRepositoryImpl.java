package com.chaeshin.boo.repository.restaurant;

import com.chaeshin.boo.domain.restaurant.Restaurant;
import com.chaeshin.boo.domain.review.Review;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class BaseRestaurantCrudRepositoryImpl implements BaseRestaurantCrudRepository {

    @PersistenceContext EntityManager em;

    /**
     * Review의 ID로 해당하는 식당 조회
     * @param reviewId
     * @return
     */
    @Override
    public Restaurant findAllByReviewId(Long reviewId) {
        return em.createQuery("select r from Review r where r.id = :reviewId", Review.class)
                .setParameter("reviewId", reviewId)
                .getSingleResult()
                .getRestaurant();
    }
}
