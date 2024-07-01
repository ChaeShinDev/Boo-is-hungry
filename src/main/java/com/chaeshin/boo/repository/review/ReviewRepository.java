package com.chaeshin.boo.repository.review;

import com.chaeshin.boo.domain.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
public interface ReviewRepository  extends JpaRepository<Review, Long> {

    /*유저ID를 통해 해당 유저의 리뷰를 이미지와 함께 모두 조회*/
    @Query("select r from Review r" +
            " join fetch r.reviewImages" +
            " where r.user.id = :userId")
    List<Review> findAllByUserIdWithReviewImages(@Param("userId") Long userId);

    /*식당ID를 통해 해당 식당의 리뷰를 이미지와 함께 모두 조회*/
    @Query("select r from Review r" +
            " join fetch r.reviewImages" +
            " where r.restaurant.id = :restaurantId")
    List<Review> findAllByRestaurantIdWithReviewImages(@Param("restaurantId") Long restaurantId);


}
