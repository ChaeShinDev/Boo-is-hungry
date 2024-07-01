package com.chaeshin.boo.repository.restaurant;

import com.chaeshin.boo.domain.restaurant.Restaurant;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface BaseRestaurantCrudRepository {

    /**
     * 이름으로 식당 조회
     *
     * @param name
     * @return
     */
    List<Restaurant> findByName(String name);


}
