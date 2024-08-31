package com.chaeshin.boo.service.restaurant.dto.geo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseMetaDto {

    @JsonProperty("total_count")
    private int totalCount; // 검색된 Document 수.

    @JsonProperty("pageable_count")
    private int pageableCount; // totalCount 중 노출 가능한 Document 수(MAX : 45)

    @JsonProperty("is_end")
    private boolean isEnd; // 현재 페이지가 마지막 페이지인지 여부. false인 경우 page 값 증가시켜 다음 페이지 요청 가능.

    @JsonProperty("same_name")
    private ResponseSameNameDto sameName;
}
