package com.chaeshin.boo.repository.restaurant;

import com.chaeshin.boo.domain.restaurant.Restaurant;

public interface BaseRestaurantCrudRepository {

    Restaurant findAllByReviewId(Long reviewId);


}
