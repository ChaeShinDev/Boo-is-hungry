package com.chaeshin.boo.repository.review;

import com.chaeshin.boo.domain.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long>,  BaseReviewCrudRepository {
}
