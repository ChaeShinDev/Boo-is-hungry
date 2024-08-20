package com.chaeshin.boo.repository.review;

import com.chaeshin.boo.domain.review.Review;
import java.util.List;

public interface BaseReviewCrudRepository {

    /**
     * 회원 ID 로 리뷰 조회.
     * <br></br>
     * Q. ReviewRepo에서 회원 ID로 리뷰 조회 VS UserRepo에서 Member Id로 검색 후 reviews 반환하기
     * @param memberId
     * @return
     */
    List<Review> findAllByMemberIdWithImage(Long memberId);

    /**
     * 식당 ID로 리뷰 조회
     * @param restaurantId
     * @return
     */
    List<Review> findAllByRestaurantIdWithImage(Long restaurantId);

    /**
     * 리뷰 ID로 이미지를 fetch join하여 조회
     * @param reviewId
     * @return
     */
    Review findByIdWithImage(Long reviewId);

    /**
     * 리뷰 ID로 번역본들을 fetch join하여 조회
     * @param reviewId
     * @return
     */
    Review findByIdWithTranslated(Long reviewId);
}
