package com.chaeshin.boo.utils.geocoding;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class GeoCodingRequestDto {

    @JsonProperty("query")
    private String address;
    @JsonProperty("analyze_type")
    private String analyzeType = "exact";

    public GeoCodingRequestDto(String address) {
        this.address = address;
    }
}
