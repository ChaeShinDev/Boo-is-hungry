package com.chaeshin.boo.service.review.dto;

import com.chaeshin.boo.domain.review.ReviewImage;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class OriginalImageDto {

    @JsonProperty("original_image")
    private String imageUrl;

    public OriginalImageDto(ReviewImage image) {
        this.imageUrl = image.getImageUrl();
    }
}
