package com.chaeshin.boo.repository.review;

import com.chaeshin.boo.domain.review.Review;
import com.chaeshin.boo.domain.review.ReviewImage;
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

    @Test
    void 리뷰_이미지_생성(){
        // given
        ReviewImage reviewImage = new ReviewImage();
        Review review = new Review();
        ReflectionTestUtils.setField(review, "id", 1L);
        reviewRepository.save(review);

        ReflectionTestUtils.setField(reviewImage, "imageUrl", "/any/photo");
        ReflectionTestUtils.setField(reviewImage, "review", review);

        // when
        ReviewImage created = reviewImageRepository.save(reviewImage);

        // then
        Assertions.assertNotNull(created);
        Assertions.assertEquals(created.getImageUrl(), reviewImage.getImageUrl());
    }

    @Test
    void 리뷰_이미지_조회(){
        // given
        ReviewImage reviewImage = new ReviewImage();
        Review review = new Review();
        ReflectionTestUtils.setField(review, "id", 1L);
        reviewRepository.save(review);

        ReflectionTestUtils.setField(reviewImage, "imageUrl", "/any/photo");
        ReflectionTestUtils.setField(reviewImage, "review", review);
        ReviewImage created = reviewImageRepository.save(reviewImage);

        // when
        Optional<ReviewImage> found = reviewImageRepository.findById(created.getId());

        // then
        Assertions.assertTrue(found.isPresent());
        Assertions.assertEquals(found.get().getId(), created.getId());

    }

    @Test
    void 리뷰_이미지_수정(){
        // given
        ReviewImage reviewImage = new ReviewImage();
        Review review = new Review();
        ReflectionTestUtils.setField(review, "id", 1L);
        reviewRepository.save(review);

        ReflectionTestUtils.setField(reviewImage, "imageUrl", "/any/photo");
        ReflectionTestUtils.setField(reviewImage, "review", review);

        ReviewImage created = reviewImageRepository.save(reviewImage);

        // when
        created.updateReviewImage("/any/changed");
        Optional<ReviewImage> found = reviewImageRepository.findById(created.getId());

        // then
        Assertions.assertTrue(found.isPresent());
        Assertions.assertEquals(found.get().getImageUrl(), created.getImageUrl());
    }

    @Test
    void 리뷰_이미지_삭제(){
        // given
        ReviewImage reviewImage = new ReviewImage();
        Review review = new Review();
        ReflectionTestUtils.setField(review, "id", 1L);
        reviewRepository.save(review);

        ReflectionTestUtils.setField(reviewImage, "imageUrl", "/any/photo");
        ReflectionTestUtils.setField(reviewImage, "review", review);

        reviewImageRepository.save(reviewImage);

        // when
        reviewImageRepository.delete(reviewImage);
        Optional<ReviewImage> found = reviewImageRepository.findById(reviewImage.getId());

        // then
        Assertions.assertFalse(found.isPresent());

    }

}
