package com.chaeshin.boo.service.restaurant.dto;

import com.chaeshin.boo.domain.restaurant.Restaurant;
import lombok.Getter;

@Getter
public class RestaurantSimpleDto {

    private Long id;
    private String name;
    private String imageUrl;

    public RestaurantSimpleDto(Restaurant restaurant) {
        this.id = restaurant.getId();
        this.name = restaurant.getName();
        this.imageUrl = restaurant.getImageUrl();
    }
}
