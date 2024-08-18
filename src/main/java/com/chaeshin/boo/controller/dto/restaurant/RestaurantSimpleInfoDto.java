package com.chaeshin.boo.controller.dto.restaurant;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Null;
import lombok.Builder;
import lombok.Data;
import java.util.List;

/**
 * 식당 간단 정보 반환을 위한 DTO.
 */
@Data
@Builder
public class RestaurantSimpleInfoDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("restaurant_image")
    @Nullable
    private String image;
}
