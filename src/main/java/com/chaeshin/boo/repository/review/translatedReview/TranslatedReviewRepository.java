package com.chaeshin.boo.repository.review.translatedReview;

import com.chaeshin.boo.domain.review.TranslatedReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TranslatedReviewRepository extends JpaRepository<TranslatedReview, Long>, BaseTranslatedReviewRepository {
}
