package com.chaeshin.boo.repository.restaurant;

import com.chaeshin.boo.domain.restaurant.Restaurant;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class RestaurantRepositoryTest {

    @Autowired RestaurantRepository restaurantRepository;

    @Test
    void 식당_저장(){

        // given
        Restaurant res = new Restaurant();
        ReflectionTestUtils.setField(res, "name", "레알라면");

        // when
        Restaurant found = restaurantRepository.save(res);

        // then
        Assertions.assertNotNull(found);
        Assertions.assertEquals(found.getName(), "레알라면");
    }

    @Test
    void 식당_Id_조회(){

        // given
        Restaurant res = new Restaurant();

        // when
        Restaurant created = restaurantRepository.save(res);
        Restaurant found = restaurantRepository.findById(created.getId()).get();

        // then
        Assertions.assertEquals(found.getId(), created.getId());
    }

    // @Test
    // void 식당_저장_이름으로_조회(){
    //     // given
    //     Restaurant restaurant = new Restaurant();
    //     ReflectionTestUtils.setField(restaurant, "name", "할머니보쌈");
    //
    //     // when
    //     restaurantRepository.save(restaurant);
    //     List<Restaurant> found = restaurantRepository.findByName("할머니 보쌈");
    //
    //     Assertions.assertFalse(found.isEmpty()); // 빈 리스트가 아닌지 체크 -> 실패!
    //
    //
    // }
}
