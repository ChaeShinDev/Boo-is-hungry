package com.chaeshin.boo.utils.geocoding;

import lombok.Getter;

@Getter
public class CoordinateDto {

    private String latitude;
    private String longitude;

    public CoordinateDto(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
