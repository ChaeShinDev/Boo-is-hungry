package com.chaeshin.boo.service.restaurant.dto;

import com.chaeshin.boo.domain.restaurant.Category;
import com.chaeshin.boo.domain.restaurant.Restaurant;
import com.chaeshin.boo.domain.review.Review;
import com.chaeshin.boo.service.review.dto.ReviewDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
public class RestaurantInfoDto {

    private Long id;
    private String name;
    private Category category;
    private String address;
    private String phone;
    @JsonProperty("restaurant_image")
    private String restaurantImage;
    @JsonProperty("opening_hours")
    private String openingHours;
    @JsonProperty("review_cnt")
    private int reviewCnt;
    @JsonProperty("scoreAvg")
    private BigDecimal scoreAvg;
    @JsonProperty("review")
    private List<ReviewDto> reviews;
    @JsonProperty("menus")
    private List<MenuDto> menus;




    public RestaurantInfoDto updateRestaurant(Restaurant restaurant) {
        this.id = restaurant.getId();
        this.name = restaurant.getName();
        this.category = restaurant.getCategory();
        this.address = restaurant.getAddress();
        this.phone = restaurant.getPhone();
        this.restaurantImage = restaurant.getImageUrl();
        this.openingHours = restaurant.getBusinessHours();
        this.reviewCnt = restaurant.getReviewCnt();
        this.scoreAvg = restaurant.getScoreAvg();
        this.menus = restaurant.getMenus().stream()
                .map(o -> new MenuDto(o)).toList();
        return this;
    }



    public RestaurantInfoDto updateReviews(List<Review> reviews) {
        this.reviews = reviews.stream()
                .map(o -> new ReviewDto(o)).toList();
        return this;
    }
}
