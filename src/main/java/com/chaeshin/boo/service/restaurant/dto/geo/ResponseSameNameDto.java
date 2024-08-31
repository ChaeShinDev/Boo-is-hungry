package com.chaeshin.boo.service.restaurant.dto.geo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseSameNameDto {

    @JsonProperty("region")
    private String[] region; // 질의어에서 인식된 지역의 리스트.

    @JsonProperty("keyword")
    private String keyword; // 질의어에서 지역 정보를 제외한 키워드

    @JsonProperty("selected_region")
    private String selectedRegion; // 인식된 지역 리스트 중 현재 검색에 사용된 지역 정보

}
