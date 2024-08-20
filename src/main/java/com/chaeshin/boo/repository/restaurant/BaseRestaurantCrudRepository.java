package com.chaeshin.boo.repository.restaurant;

import com.chaeshin.boo.domain.restaurant.Restaurant;

import java.util.List;

public interface BaseRestaurantCrudRepository {

    Restaurant findByReviewId(Long reviewId);

    Restaurant findByIdWithMenus(Long restaurantId);

    List<Restaurant> findAllByNameContaining(String name);
}
