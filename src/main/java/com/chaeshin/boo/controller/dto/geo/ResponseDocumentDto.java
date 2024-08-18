package com.chaeshin.boo.controller.dto.geo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

/**
 * 특정 장소(식당, 빌딩, 카페, 약국 등...)의 위치  링크 URL, 카테고리 등에 관한 정보가 담긴 응답을 위한 DTO.
 */
@Data
@Builder
public class ResponseDocumentDto {

    private String id;

    @JsonProperty("place_name")
    private String name;

    @JsonProperty("category_name")
    private String categoryName;

    @JsonProperty("category_group_code")
    private String categoryGroupCode;

    @JsonProperty("category_group_name")
    private String categoryGroupName;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("address_name")
    private String addressName;

    @JsonProperty("road_address_name")
    private String roadAddressName;

    @JsonProperty("x")
    private String longitude;

    @JsonProperty("y")
    private String latitude;

    @JsonProperty("place_url")
    private String placeUrl;

    @JsonProperty("distance")
    private String distance;
}
