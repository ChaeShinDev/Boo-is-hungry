package com.chaeshin.boo.repository.restaurant.menu;

import com.chaeshin.boo.domain.restaurant.Menu;
import java.util.List;


public interface BaseMenuCrudRepository {

    /**
     * 주어진 식당의 메뉴 전체 반환
     *
     * @param restaurantId
     * @return
     */
    List<Menu> findAllByRestaurantId(Long restaurantId);
}