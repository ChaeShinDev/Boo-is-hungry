package com.chaeshin.boo.repository.review.translatedReview;

import com.chaeshin.boo.domain.review.TranslatedReview;
import org.springframework.data.repository.CrudRepository;

public interface TranslatedReviewRepository extends CrudRepository<TranslatedReview, Long>, BaseTranslatedReviewRepository {
}
