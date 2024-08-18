package com.chaeshin.boo.service.review;

import com.chaeshin.boo.controller.dto.review.ReviewDto;
import com.chaeshin.boo.domain.Member;
import com.chaeshin.boo.domain.restaurant.Category;
import com.chaeshin.boo.domain.restaurant.Restaurant;
import com.chaeshin.boo.domain.review.Review;
import com.chaeshin.boo.exception.NoSuchReviewException;
import com.chaeshin.boo.service.member.MemberService;
import com.chaeshin.boo.service.restaurant.RestaurantService;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class ReviewServiceTest {

    @Autowired
    ReviewService reviewService;

    @Autowired
    RestaurantService restaurantService;

    @Autowired
    MemberService memberService;

    Restaurant restaurant;

    String memberGmail;

    @BeforeEach
    void setUp() {
        restaurant = Restaurant.builder()
                .name("제발되라식당")
                .address("경남 사천시")
                .phone("1577")
                .category(Category.KOREAN)
                .build();

        memberGmail = "janedoe@gmail.com";
    }

    @Test
    @DisplayName("리뷰 생성")
    void createReview() {
        // given
        Long memberId = memberService.join(memberGmail);
        Restaurant createdRestaurant = restaurantService.createRestaurant(restaurant);
        Member member = memberService.getMemberById(memberId).get();

        Review newReview = Review.builder()
                .restaurant(createdRestaurant)
                .title("맛있으면 짖는 개")
                .body("크르르르르르ㅡ르릉")
                .member(member)
                .score(1)
                .build();

        // when
        Long reviewId = reviewService.createReview(newReview);
        Review foundReview = reviewService.getReviewById(reviewId);

        // then
        Assertions.assertEquals(newReview.getTitle(), foundReview.getTitle());
        Assertions.assertNotNull(foundReview.getLangCode()); // LangCode 초기화가 잘 되었는지 확인.
    }

    @Test
    @DisplayName("리뷰 ID로 조회")
    void getReviewById() {
        // given
        Long memberId = memberService.join(memberGmail);
        Restaurant createdRestaurant = restaurantService.createRestaurant(restaurant);
        Member member = memberService.getMemberById(memberId).get();

        Review newReview = Review.builder()
                .restaurant(createdRestaurant)
                .title("맛있으면 짖는 개")
                .body("크르르르르르ㅡ르릉")
                .member(member)
                .score(1)
                .build();

        // when
        Long reviewId = reviewService.createReview(newReview);
        Review foundReview = reviewService.getReviewById(reviewId);
        // then
        Assertions.assertNotNull(foundReview);
        Assertions.assertEquals(newReview.getTitle(), foundReview.getTitle());
    }

    @Test
    @DisplayName("멤버 ID로 리뷰 전체 조회")
    void getAllByMemberIdWithImage() {
        // given
        Long memberId = memberService.join(memberGmail);
        Restaurant createdRestaurant = restaurantService.createRestaurant(restaurant);
        Member member = memberService.getMemberById(memberId).get();

        Review newReview = Review.builder()
                .restaurant(createdRestaurant)
                .title("맛있으면 짖는 개")
                .body("크르르르르르ㅡ르릉")
                .member(member)
                .score(1)
                .build();

        // when
        Long reviewId = reviewService.createReview(newReview);
        List<Review> reviews = reviewService.getAllByMemberIdWithImage(memberId);

        // then
        Assertions.assertNotNull(reviews);
        Assertions.assertEquals(1, reviews.size());
    }

    @Test
    @DisplayName("식당 ID로 리뷰 전체 조회")
    void getAllByRestaurantIdWithImage() {
        // given
        Long memberId = memberService.join(memberGmail);
        Restaurant createdRestaurant = restaurantService.createRestaurant(restaurant);
        Member member = memberService.getMemberById(memberId).get();

        Review newReview = Review.builder()
                .restaurant(createdRestaurant)
                .title("맛있으면 짖는 개")
                .body("크르르르르르ㅡ르릉")
                .member(member)
                .score(1)
                .build();
        // when
        Long reviewId = reviewService.createReview(newReview);
        List<Review> reviews = reviewService.getAllByRestaurantIdWithImage(createdRestaurant.getId());

        // then
        Assertions.assertNotNull(reviews);
        Assertions.assertEquals(1, reviews.size());

    }

    @Test
    @DisplayName("리뷰 업데이트")
    void updateReview() {
        // given
        Long memberId = memberService.join(memberGmail);
        Restaurant createdRestaurant = restaurantService.createRestaurant(restaurant);
        Member member = memberService.getMemberById(memberId).get();

        Review newReview = Review.builder()
                .restaurant(createdRestaurant)
                .title("맛있으면 짖는 개")
                .body("크르르르르르ㅡ르릉")
                .member(member)
                .score(1)
                .build();

        Long reviewId = reviewService.createReview(newReview);

        // when
        ReviewDto reviewDto = ReviewDto.builder()
                .title("맛있으면 짖는 개")
                .body("UPDATED BODY")
                .score(5)
                .build();

        reviewService.updateReview(reviewId, reviewDto);

        Review updatedReview = reviewService.getReviewById(reviewId);

        // then
        Assertions.assertNotNull(updatedReview);
        Assertions.assertEquals(reviewDto.getBody(), updatedReview.getBody());
    }

    @Test
    @DisplayName("리뷰 삭제")
    void deleteReview() {
        // given
        Long memberId = memberService.join(memberGmail);
        Restaurant createdRestaurant = restaurantService.createRestaurant(restaurant);
        Member member = memberService.getMemberById(memberId).get();
        Review newReview = Review.builder()
                .restaurant(createdRestaurant) // @Transactional 없을 시 LazyLoadingException 발생.
                .title("맛있으면 짖는 개")
                .body("크르르르르르ㅡ르릉")
                .member(member)
                .score(1)
                .build();

        Long reviewId = reviewService.createReview(newReview);

        // when
        reviewService.deleteReview(reviewId);

        // then
        Assertions.assertThrows(NoSuchReviewException.class, () -> reviewService.getReviewById(reviewId));
    }
}
