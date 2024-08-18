package com.chaeshin.boo.controller.dto.restaurant;

import com.chaeshin.boo.domain.restaurant.Category;
import com.chaeshin.boo.domain.restaurant.Menu;
import com.chaeshin.boo.domain.review.Review;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import java.math.BigDecimal;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RestaurantDetailInfoDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("latitude")
    private String latitude;

    @JsonProperty("longitude")
    private String longitude;

    @JsonProperty("opening_hours")
    @Nullable
    private String businessHours;

    @JsonProperty("address")
    private String address;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("review_cnt")
    private int reviewCnt;

    @JsonProperty("score_avg")
    private BigDecimal scoreAvg;

    @JsonProperty("category")
    private Category category;

    @JsonProperty("menu")
    private List<Menu> menus;

    @JsonProperty("review")
    private List<Review> reviews;
}
