package com.chaeshin.boo.controller.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class CreatedReviewImageDto extends BaseResponseDto {
    private String msg;
    private String original_image; // Original Image URL
    private String review_image; // Preview image(Thumbnail) URL
}
