package com.chaeshin.boo.repository;

import com.chaeshin.boo.domain.restaurant.Category;
import com.chaeshin.boo.domain.restaurant.Menu;
import com.chaeshin.boo.domain.restaurant.Restaurant;
import com.chaeshin.boo.domain.review.Review;
import com.chaeshin.boo.repository.restaurant.MenuRepository;
import com.chaeshin.boo.repository.restaurant.RestaurantRepository;
import com.chaeshin.boo.repository.review.ReviewImageRepository;
import com.chaeshin.boo.repository.review.ReviewRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
public class RestaurantRepositoryTest {

    @Autowired
    RestaurantRepository restaurantRepository;
    @Autowired
    MenuRepository menuRepository;
    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    ReviewImageRepository reviewImageRepository;

    @Test
    public void 식당_생성() throws Exception {
        /*given*/
        Restaurant restaurant = createRestaurant();

        /*when*/
        Restaurant savedRestaurant = restaurantRepository.save(restaurant);

        /*then*/
        Assertions.assertEquals(restaurant, savedRestaurant);
    }


    @Test
    public void 리뷰로_식당_조회() throws Exception {
        Restaurant restaurant = createRestaurant();
        /*given*/
        restaurantRepository.save(restaurant);

        Review review = createReview(restaurant);
        reviewRepository.save(review);

        restaurant.getReviews().add(review);

        /*when*/
        Restaurant searched = restaurantRepository.findByReviewId(review.getId());

        /*then*/
        Assertions.assertEquals(restaurant, searched);
    }


    @Test
    public void 메뉴와_함께_식당_조회() throws Exception {
        /*given*/
        Restaurant restaurant = restaurantRepository.save(createRestaurant());

        /*when*/
        Menu menu = menuRepository.save(createMenu(restaurant));
        restaurant.getMenus().add(menu);

        Restaurant searched = restaurantRepository.findByIdWithMenus(restaurant.getId());

        /*then*/

        assertEquals(restaurant, searched);
        assertEquals(searched.getMenus().get(0), menu);
    }


    @Test
    public void 식당_이름으로_조회() throws Exception {
        /*given*/
        Restaurant restaurant = restaurantRepository.save(createRestaurant());
        String rName = restaurant.getName();

        Restaurant fakeRestaurant = new Restaurant().builder().name("외대").build();

        /*when*/
        List<Restaurant> searchedList = restaurantRepository.findAllByNameContaining(rName);

        /*then*/
        assertNotNull(searchedList);
        assertEquals(searchedList.size(), 1);
        assertEquals(searchedList.get(0), restaurant);
    }





    private Restaurant createRestaurant() {
        return new Restaurant().builder().name("맥도날드").latitude("위도").longitude("경도")
                .category(Category.KOREAN).build();
    }

    private Menu createMenu(Restaurant restaurant) {
        return new Menu().builder().restaurant(restaurant).name("햄버거").build();
    }

    private Review createReview(Restaurant restaurant) {
        return new Review().builder().restaurant(restaurant).build();
    }
}
