package com.chaeshin.boo.service.restaurant.dto;

import com.chaeshin.boo.domain.restaurant.Category;
import com.chaeshin.boo.domain.restaurant.Restaurant;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class RestaurantDetailDto {

    private Long id;
    private String name;
    @JsonProperty("restaurant_image")
    private String imageUrl;
    private String latitude;
    private String longitude;
    @JsonProperty("opening_hours")
    private String businessHours;
    private String address;
    private String phone;
    @JsonProperty("review_cnt")
    private int reviewCnt;
    @JsonProperty("score_accum")
    private int scoreAccum;
    @JsonProperty("score_avg")
    private BigDecimal scoreAvg;
    private Category category;

    public RestaurantDetailDto(Restaurant restaurant) {
        this.id = restaurant.getId();
        this.name = restaurant.getName();
        this.imageUrl = restaurant.getImageUrl();
        this.latitude = restaurant.getLatitude();
        this.longitude = restaurant.getLongitude();
        this.businessHours = restaurant.getBusinessHours();
        this.address = restaurant.getAddress();
        this.phone = restaurant.getPhone();
        this.reviewCnt = restaurant.getReviewCnt();
        this.scoreAccum = restaurant.getScoreAccum();
        this.scoreAvg = restaurant.getScoreAvg();
        this.category = restaurant.getCategory();
    }
}
