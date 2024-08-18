package com.chaeshin.boo.repository.restaurant;

import com.chaeshin.boo.domain.restaurant.Restaurant;
import java.util.Optional;

public interface BaseRestaurantCrudRepository {

    Restaurant findByReviewId(Long reviewId);


}
