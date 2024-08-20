package com.chaeshin.boo.service.restaurant.dto;

import com.chaeshin.boo.domain.restaurant.Menu;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class MenuDto {

    private Long id;
    private String name;
    @JsonProperty("menu_image")
    private String imageUrl;
    @JsonProperty("restaurant")
    private Long restaurantId;

    public MenuDto(Menu menu) {
        this.id = menu.getId();
        this.name = menu.getName();
        this.imageUrl = menu.getImageUrl();
        this.restaurantId = menu.getRestaurant().getId();
    }
}
