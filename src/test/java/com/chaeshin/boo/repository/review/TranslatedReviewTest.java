package com.chaeshin.boo.repository.review;

import com.chaeshin.boo.domain.review.TranslatedReview;
import com.chaeshin.boo.repository.review.translatedReview.BaseTranslatedReviewRepository;
import com.chaeshin.boo.repository.review.translatedReview.TranslatedReviewRepository;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class TranslatedReviewTest {

    @Autowired
    TranslatedReviewRepository translatedReviewRepository;
    @Autowired ReviewRepository reviewRepository;

    /**
     * @NotNull 제약 추가시 테스트 수정해야함.
     */
    @Test
    void 리뷰_번역_생성(){
        // given
        TranslatedReview transR = new TranslatedReview();
        ReflectionTestUtils.setField(transR, "body", "Translated");

        // when
        TranslatedReview created = translatedReviewRepository.save(transR);

        // then
        Assertions.assertNotNull(created);
        Assertions.assertEquals(created.getBody(), transR.getBody());
    }

    @Test
    void 리뷰_번역_조회(){
        // given
        TranslatedReview transR = new TranslatedReview();
        ReflectionTestUtils.setField(transR, "body", "Translated");
        ReflectionTestUtils.setField(transR, "id", 1L);

        // when
        translatedReviewRepository.save(transR);
        Optional<TranslatedReview> found = translatedReviewRepository.findById(transR.getId());

        // then
        Assertions.assertTrue(found.isPresent());
        Assertions.assertEquals(found.get().getId(), transR.getId());
    }

    @Test
    void 리뷰_번역_수정(){
        // given
        TranslatedReview transR = new TranslatedReview();
        ReflectionTestUtils.setField(transR, "body", "Translated");
        ReflectionTestUtils.setField(transR, "id", 1L);

        // when
        translatedReviewRepository.save(transR);
        translatedReviewRepository.updateTranslatedReview(1L, "changed!");

        Optional<TranslatedReview> found = translatedReviewRepository.findById(1L);

        // then
        Assertions.assertTrue(found.isPresent());
        Assertions.assertEquals(found.get().getBody(), "changed!");


    }

    @Test
    void 리뷰_번역_삭제(){
        // given
        TranslatedReview transR = new TranslatedReview();
        ReflectionTestUtils.setField(transR, "body", "Translated");
        ReflectionTestUtils.setField(transR, "id", 1L);

        TranslatedReview created = translatedReviewRepository.save(transR);

        // when
        translatedReviewRepository.delete(created);
        Optional<TranslatedReview> found = translatedReviewRepository.findById(created.getId());

        // then
        Assertions.assertFalse(found.isPresent());
    }


}
