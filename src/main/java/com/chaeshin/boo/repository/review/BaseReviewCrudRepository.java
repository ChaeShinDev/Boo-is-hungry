package com.chaeshin.boo.repository.review;

import com.chaeshin.boo.domain.review.Review;
import java.util.List;

public interface BaseReviewCrudRepository {

    /**
     * 회원 ID 로 리뷰 조회.
     * <br></br>
     * Q. ReviewRepo에서 회원 ID로 리뷰 조회 VS UserRepo에서 User Id로 검색 후 reviews 반환하기
     * @param userId
     * @return
     */
    List<Review> findAllByUserId(Long userId);

    /**
     * 식당 ID로 리뷰 조회
     * @param restaurantId
     * @return
     */
    List<Review> findAllByRestaurantId(Long restaurantId);

}
