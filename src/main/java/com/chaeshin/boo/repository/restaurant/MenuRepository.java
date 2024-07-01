package com.chaeshin.boo.repository.restaurant;

import com.chaeshin.boo.domain.restaurant.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    /*식당ID로 메뉴 조회*/
    @Query("select m from Menu m" +
            " where m.restaurant.id = :restaurantId")
    List<Menu> findAllByRestaurantId(@Param("restaurantId") Long restaurantId);
}
