package com.chaeshin.boo.utils.geocoding;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class GeoCodingResponseDto {

    private List<Documents> documents;
    private Meta meta;

    @Data
    static class Documents {
        private String x; // longitude
        private String y; // latitude
    }

    @Data
    static class Meta {
        @JsonProperty("total_count")
        private int totalCount;
    }

    public CoordinateDto toCoordinateDto() {
        return new CoordinateDto(documents.get(0).getY(), documents.get(0).getX());
    }
}
