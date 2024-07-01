package com.chaeshin.boo.repository.review;

import com.chaeshin.boo.domain.review.Review;
import org.springframework.data.repository.CrudRepository;

public interface ReviewRepository extends CrudRepository<Review, Long>,  BaseReviewCrudRepository {
}
