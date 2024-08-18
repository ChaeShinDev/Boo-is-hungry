package com.chaeshin.boo.service.review.translatedReview;

import static org.junit.jupiter.api.Assertions.*;

import com.chaeshin.boo.domain.LangCode;
import com.chaeshin.boo.domain.Member;
import com.chaeshin.boo.domain.restaurant.Category;
import com.chaeshin.boo.domain.restaurant.Restaurant;
import com.chaeshin.boo.domain.review.Review;
import com.chaeshin.boo.domain.review.TranslatedReview;
import com.chaeshin.boo.service.member.MemberService;
import com.chaeshin.boo.service.restaurant.RestaurantService;
import com.chaeshin.boo.service.review.ReviewService;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class TranslatedReviewServiceTest {

    @Autowired
    TranslatedReviewService translatedReviewService;

    @Autowired
    MemberService memberService;

    @Autowired
    RestaurantService restaurantService;

    @Autowired
    ReviewService reviewService;

    // 테스트를 위한 멤버, 레스토랑, 리뷰 객체 생성.
    Member member;
    Restaurant restaurant;
    Review review;


    @BeforeEach
    void setUp() {
        Long memberId = memberService.join("johndoe@gmail.com");
        restaurant = Restaurant.builder()
                .name("제발되라식당")
                .address("경남 사천시")
                .phone("1577")
                .category(Category.KOREAN)
                .build();

        member = memberService.getMemberById(memberId).get();
        restaurant = restaurantService.createRestaurant(restaurant);

        review = Review.builder()
                .restaurant(restaurant)
                .member(member)
                .title("title")
                .body("body")
                .score(3)
                .build();

        Long reviewId = reviewService.createReview(review);
        review = reviewService.getReviewById(reviewId);
    }

    @Test
    @DisplayName("번역 리뷰 생성 및 조회")
    void createTranslatedReview() {
        // when
        TranslatedReview translatedReview = translatedReviewService.createTranslatedReview(review, LangCode.BG);
        Long translatedReviewId = translatedReview.getId();

        // then
        Assertions.assertNotNull(translatedReview);
        Assertions.assertEquals(translatedReview, translatedReviewService.getTranslatedReviewById(translatedReviewId));
    }

    @Test
    @DisplayName("모든 번역 리뷰 조회")
    void getAllTranslatedReview() {

        // when
        TranslatedReview spanish = translatedReviewService.createTranslatedReview(review, LangCode.ES);
        TranslatedReview russian = translatedReviewService.createTranslatedReview(review, LangCode.RU);

        // then
        Assertions.assertNotNull(translatedReviewService.getAllTranslatedReview(review.getId()));
        // 리뷰 생성 시 회원의 선호 언어와 작성된 리뷰의 언어가 다른 경우 자동으로 선호 언어 번역을 생성한다. 따라서 3개가 생성되어야 한다.
        Assertions.assertEquals(3, translatedReviewService.getAllTranslatedReview(review.getId()).size());
    }
}