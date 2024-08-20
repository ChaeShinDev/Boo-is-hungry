package com.chaeshin.boo.service.review.dto;

import com.chaeshin.boo.domain.review.ReviewImage;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class ReviewImageDto {
    private Long id;
    @JsonProperty("review")
    private Long reviewId;
    @JsonProperty("review_image")
    private String imageUrl;

    public ReviewImageDto(ReviewImage image) {
        this.id = image.getId();
        this.reviewId = image.getReview().getId();
        this.imageUrl = image.getImageUrl();
    }
}
