package com.chaeshin.boo.repository.review;

import com.chaeshin.boo.domain.Member;
import com.chaeshin.boo.domain.restaurant.Restaurant;
import com.chaeshin.boo.domain.review.Review;
import com.chaeshin.boo.domain.review.ReviewImage;
import com.chaeshin.boo.repository.member.MemberRepository;
import com.chaeshin.boo.repository.restaurant.RestaurantRepository;
import com.chaeshin.boo.repository.review.reviewImage.ReviewImageRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class ReviewRepositoryTest {

    @Autowired ReviewRepository reviewRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired RestaurantRepository restaurantRepository;

    @Autowired ReviewImageRepository reviewImageRepository;

    @Test
    void 리뷰_저장(){
        // given
        Member member = new Member();
        Restaurant res = new Restaurant();
        Member savedMember = memberRepository.save(member);
        Restaurant savedRes = restaurantRepository.save(res);

        Review review = Review.builder().member(savedMember).restaurant(savedRes).build();

        // when
        Review savedReview = reviewRepository.save(review);

        // then
        Assertions.assertNotNull(savedReview);

    }

    @Test
    void 리뷰_ID_조회(){

        // given
        Member member = new Member();
        Restaurant res = new Restaurant();

        Member savedMember = memberRepository.save(member);
        Restaurant savedRes = restaurantRepository.save(res);

        Review review = Review.builder().member(savedMember).restaurant(savedRes).build();
        Review savedReview = reviewRepository.save(review);

        // when
        Optional<Review> foundReview = reviewRepository.findById(savedReview.getId());

        // then
        Assertions.assertTrue(foundReview.isPresent());
        Assertions.assertEquals(foundReview.get().getId(), savedReview.getId());
    }

    @Test
    void 회원_ID_리뷰_이미지_함깨_조회(){
        // given
        Member member = new Member();
        Restaurant res = new Restaurant();

        Member savedMember = memberRepository.save(member);
        Restaurant savedRes = restaurantRepository.save(res);

        Review review = Review.builder().member(savedMember).restaurant(savedRes).build();
        ReviewImage reviewImage = ReviewImage.builder().review(review).imageUrl("/test").build();

        Review savedReview = reviewRepository.save(review);
        reviewImageRepository.save(reviewImage);

        // when
        List<Review> foundReview = reviewRepository.findAllByMemberIdWithImage(savedMember.getId());

        // then
        Assertions.assertFalse(foundReview.isEmpty());
        Assertions.assertTrue(foundReview.contains(savedReview)); // Internally checks if `.equals(Item i, Object O)'.
        Assertions.assertEquals(1, foundReview.get(0).getReviewImages().size());

    }

    @Test
    void 식당_ID_리뷰_이미지_함께_조회(){
        // given
        Member member = new Member();
        Restaurant res = new Restaurant();

        Member savedMember = memberRepository.save(member);
        Restaurant savedRes = restaurantRepository.save(res);

        Review review = Review.builder().member(savedMember).restaurant(savedRes).build();
        ReviewImage reviewImage = ReviewImage.builder().review(review).imageUrl("/test").build();

        Review savedReview = reviewRepository.save(review);
        reviewImageRepository.save(reviewImage);

        // when
        List<Review> foundReview = reviewRepository.findAllByRestaurantIdWithImage(res.getId());

        // then
        Assertions.assertFalse(foundReview.isEmpty());
        Assertions.assertTrue(foundReview.contains(savedReview));
        Assertions.assertEquals(1, foundReview.get(0).getReviewImages().size());
    }

    @Test
    void 리뷰_삭제(){
        // given
        Member member = new Member();
        Restaurant res = new Restaurant();

        Member savedMember = memberRepository.save(member);
        Restaurant savedRes = restaurantRepository.save(res);

        Review review = Review.builder().member(savedMember).restaurant(savedRes).build();
        Review savedReview = reviewRepository.save(review);

        // when
        reviewRepository.delete(savedReview);
        Optional<Review> found = reviewRepository.findById(savedReview.getId());

        // then
        Assertions.assertFalse(found.isPresent());
    }
}
