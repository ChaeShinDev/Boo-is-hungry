package com.chaeshin.boo.repository.review;

import com.chaeshin.boo.domain.review.Review;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BaseReviewCrudRepositoryImpl implements BaseReviewCrudRepository {

    @PersistenceContext EntityManager em;

    /**
     * 회원 ID 로 리뷰 조회.
     * <br></br>
     * Q. 'Pageable' 도입하는 것이 좋지 않을까?
     * @param memberId
     * @return
     */
    @Override
    public List<Review> findAllByMemberIdWithImage(Long memberId) {
        return em.createQuery("select r from Review r"
                + " left join fetch r.reviewImages"
                + " where r.member.id = :memberId", Review.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }

    /**
     * 식당 ID 로 리뷰 조회
     * @param restaurantId
     * @return
     */
    @Override
    public List<Review> findAllByRestaurantIdWithImage(Long restaurantId) {
        return em.createQuery("select r from Review r left join fetch r.reviewImages where r.restaurant.id = :restaurantId", Review.class)
                .setParameter("restaurantId", restaurantId)
                .getResultList();
    }

    @Override
    public Review findByIdWithImage(Long reviewId) {
        return em.createQuery("select r from Review r" +
                " left join fetch r.reviewImages" +
                " where r.id =:reviewId", Review.class)
                .setParameter("reviewId", reviewId)
                .getSingleResult();
    }

    @Override
    public Review findByIdWithTranslated(Long reviewId) {
        return em.createQuery("select r from Review  r" +
                " left join fetch r.translatedReviews" +
                " where r.id =:reviewId", Review.class)
                .setParameter("reviewId", reviewId)
                .getSingleResult();
    }
}
