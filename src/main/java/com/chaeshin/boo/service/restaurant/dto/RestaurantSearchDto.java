package com.chaeshin.boo.service.restaurant.dto;

import com.chaeshin.boo.domain.restaurant.Category;
import com.chaeshin.boo.domain.restaurant.Restaurant;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class RestaurantSearchDto {

    private Long id;
    @JsonProperty("restaurant_image")
    private String imageUrl;
    private String name;
    private String address;
    private Category category;
    @JsonProperty("review_cnt")
    private int reviewCnt;
    @JsonProperty("score_avg")
    private BigDecimal scoreAvg;

    public RestaurantSearchDto(Restaurant restaurant) {
        this.id = restaurant.getId();
        this.imageUrl = restaurant.getImageUrl();
        this.name = restaurant.getName();
        this.address = restaurant.getAddress();
        this.category = restaurant.getCategory();
        this.reviewCnt = restaurant.getReviewCnt();
        this.scoreAvg = restaurant.getScoreAvg();
    }
}
