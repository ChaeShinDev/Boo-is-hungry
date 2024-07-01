package com.chaeshin.boo.repository;

import com.chaeshin.boo.domain.restaurant.Category;
import com.chaeshin.boo.domain.restaurant.Menu;
import com.chaeshin.boo.domain.restaurant.Restaurant;
import com.chaeshin.boo.repository.restaurant.MenuRepository;
import com.chaeshin.boo.repository.restaurant.RestaurantRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class MenuRepositoryTest {

    @Autowired MenuRepository menuRepository;
    @Autowired RestaurantRepository restaurantRepository;


    @Test
    public void 식당의_메뉴_리스트_조회() throws Exception {
        /*given*/
        Restaurant restaurant = restaurantRepository.save(createRestaurant());

        for (int i = 0; i < 10; i++) {
            Menu menu = menuRepository.save(createMenu(restaurant));
            restaurant.getMenus().add(menu);
        }

        /*when*/
        List<Menu> menus = menuRepository.findAllByRestaurantId(restaurant.getId());

        /*then*/
        assertEquals(10, menus.size());
        assertEquals(10, restaurant.getMenus().size());
    }



    private Restaurant createRestaurant() {
        return new Restaurant().builder().name("맥도날드").latitude("위도").longitude("경도")
                .category(Category.KOREAN).build();
    }

    private Menu createMenu(Restaurant restaurant) {
        return new Menu().builder().restaurant(restaurant).name("햄버거").build();
    }
}
