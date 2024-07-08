package com.chaeshin.boo.repository.restaurant;

import com.chaeshin.boo.domain.restaurant.Restaurant;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface BaseRestaurantCrudRepository {

    Restaurant findByIdWithMenu(Long restaurantId);


}
