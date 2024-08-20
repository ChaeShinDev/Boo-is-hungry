package com.chaeshin.boo.service.review.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class ImageUploadDto {

    @JsonProperty("original_image")
    private String originalImage;
    @JsonProperty("review_image")
    private String thumbnailImage;

    public ImageUploadDto(String url) {
        this.originalImage = url;
        this.thumbnailImage = url;
    }
}
