package com.chaeshin.boo.service.restaurant.dto;

import com.chaeshin.boo.domain.restaurant.Category;
import com.chaeshin.boo.domain.restaurant.Restaurant;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MapInfoDto {

    private Long id;
    private String name;
    private String latitude;
    private String longitude;
    private Category category;
    private String address;
    private String phone;
    @JsonProperty("review_cnt")
    private int reviewCnt;
    @JsonProperty("scoreAvg")
    private BigDecimal scoreAvg;

    public MapInfoDto(Restaurant restaurant) {
        this.id = restaurant.getId();
        this.name = restaurant.getName();
        this.latitude = restaurant.getLatitude();
        this.longitude = restaurant.getLongitude();
        this.category = restaurant.getCategory();
        this.address = restaurant.getAddress();
        this.phone = restaurant.getPhone();
        this.reviewCnt = restaurant.getReviewCnt();
        this.scoreAvg = restaurant.getScoreAvg();
    }
}
