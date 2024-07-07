package com.chaeshin.boo.repository.review;

import com.chaeshin.boo.domain.Member;
import com.chaeshin.boo.domain.review.Review;
import com.chaeshin.boo.domain.review.ReviewImage;
import com.chaeshin.boo.repository.member.MemberRepository;
import com.chaeshin.boo.repository.review.reviewImage.ReviewImageRepository;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

/**
 * 현재 테스트 메서드 단위로 실행 시 통과. 하지만 클래스 단위로 실행시 실패. 뭐가 문제일까?
 */
@SpringBootTest
@Transactional
public class ReviewImageRepositoryTest {

    @Autowired ReviewImageRepository reviewImageRepository;
    @Autowired ReviewRepository reviewRepository;
    @Autowired MemberRepository memberRepository;

    @Test
    void 리뷰_이미지_생성(){

        // given
        Member member = new Member();
        Member savedMember = memberRepository.save(member);

        Review review = new Review();
        review.updateMember(savedMember);
        Review savedReview = reviewRepository.save(review);

        ReviewImage reviewImage = ReviewImage.createReviewImage(savedReview);
        ReflectionTestUtils.setField(reviewImage, "imageUrl", "/any/photo");

        // when
        ReviewImage savedImage = reviewImageRepository.save(reviewImage);

        // then
        Assertions.assertTrue(reviewImageRepository.findById(savedImage.getId()).isPresent());
    }

    @Test
    void 리뷰_이미지_조회(){

        // given
        Member member = new Member();
        Member savedMember = memberRepository.save(member);

        Review review = new Review();
        review.updateMember(savedMember);
        Review savedReview = reviewRepository.save(review);

        ReviewImage reviewImage = ReviewImage.createReviewImage(savedReview);
        ReflectionTestUtils.setField(reviewImage, "imageUrl", "/any/photo");

        ReviewImage savedImage = reviewImageRepository.save(reviewImage);

        // when
        Optional<ReviewImage> found = reviewImageRepository.findById(savedImage.getId());

        // then
        Assertions.assertTrue(found.isPresent());
        Assertions.assertEquals(found.get().getId(), savedImage.getId());

    }

    @Test
    void 리뷰_이미지_수정(){
        // given
        Member member = new Member();
        Member savedMember = memberRepository.save(member);

        Review review = new Review();
        review.updateMember(savedMember);
        Review savedReview = reviewRepository.save(review);

        ReviewImage reviewImage = ReviewImage.createReviewImage(savedReview);
        ReflectionTestUtils.setField(reviewImage, "imageUrl", "/any/photo");

        ReviewImage savedImage = reviewImageRepository.save(reviewImage);


        // when
        savedImage.updateReviewImage("/any/changed");
        Optional<ReviewImage> found = reviewImageRepository.findById(savedImage.getId());

        // then
        Assertions.assertTrue(found.isPresent());
        Assertions.assertEquals(found.get().getImageUrl(), savedImage.getImageUrl());
    }

    @Test
    void 리뷰_이미지_삭제(){
        // given
        Member member = new Member();
        Member savedMember = memberRepository.save(member);

        Review review = new Review();
        review.updateMember(savedMember);
        Review savedReview = reviewRepository.save(review);

        ReviewImage reviewImage = ReviewImage.createReviewImage(savedReview);
        ReflectionTestUtils.setField(reviewImage, "imageUrl", "/any/photo");

        ReviewImage savedImage = reviewImageRepository.save(reviewImage);


        // when
        reviewImageRepository.delete(savedImage);
        Optional<ReviewImage> found = reviewImageRepository.findById(savedImage.getId());

        // then
        Assertions.assertFalse(found.isPresent());

    }

}
