package com.chaeshin.boo.repository.restaurant;

import com.chaeshin.boo.domain.restaurant.Menu;
import com.chaeshin.boo.domain.restaurant.Restaurant;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class MenuRepositoryTest {

    @Autowired MenuRepository menuRepository;
    @Autowired RestaurantRepository restaurantRepository;

    @Test
    void 식당으로_메뉴_조회(){

        // Scenario
        // 식당 : {name : "레알라면, id : 1L, Menu : "라면"}
        // 메뉴 : {name : "라면", Restaurant_id : 1L}

        // given
        Restaurant restaurant = new Restaurant();
        ReflectionTestUtils.setField(restaurant, "name", "레알라면");
        ReflectionTestUtils.setField(restaurant, "id", 1L);
        restaurantRepository.save(restaurant);

        // when
        Menu menu = new Menu();
        ReflectionTestUtils.setField(menu, "restaurant", restaurant);
        ReflectionTestUtils.setField(menu, "name", "라면");

        menuRepository.save(menu);

        // then
        List<Menu> found = menuRepository.findAllByRestaurant(restaurant.getId());

        Assertions.assertFalse(found.isEmpty());
        Assertions.assertEquals(found.get(0).getName(), "라면");

    }
}
