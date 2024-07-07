package com.chaeshin.boo.repository.review.translatedReview;

import com.chaeshin.boo.domain.review.TranslatedReview;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface BaseTranslatedReviewRepository {

    List<TranslatedReview> findAllByReviewId(Long reviewId);

    /**
     * 번역 수정
     * @param newBody
     */
    void updateTranslatedReview(Long id, String newBody);
}
