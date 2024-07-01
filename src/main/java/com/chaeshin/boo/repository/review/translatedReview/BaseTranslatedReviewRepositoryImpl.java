package com.chaeshin.boo.repository.review.translatedReview;

import com.chaeshin.boo.domain.review.TranslatedReview;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class BaseTranslatedReviewRepositoryImpl implements BaseTranslatedReviewRepository {

    @PersistenceContext EntityManager em;

    /**
     * 번역 본문 수정
     * @param newBody
     */
    @Override
    public void updateTranslatedReview(Long id, String newBody) {
        TranslatedReview found = em.find(TranslatedReview.class, id);
        found.updateBody(newBody);
    }
}
