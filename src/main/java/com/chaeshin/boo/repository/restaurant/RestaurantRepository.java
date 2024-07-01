package com.chaeshin.boo.repository.restaurant;

import com.chaeshin.boo.domain.restaurant.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    /*리뷰ID를 통해 식당 정보 조회*/
    @Query("select rv.restaurant from Review rv" +
            " where rv.id = :reviewId")
    Restaurant findByReviewId(@Param("reviewId") Long reviewId);

    /*식당ID를 통해 식당 정보와 메뉴 조회*/
    @Query("select rt from Restaurant rt" +
            " join fetch rt.menus" +
            " where rt.id = :id")
    Restaurant findByIdWithMenus(@Param("id") Long id);

    /*식당 이름으로 조회*/
    List<Restaurant> findAllByNameContaining(String name);
}