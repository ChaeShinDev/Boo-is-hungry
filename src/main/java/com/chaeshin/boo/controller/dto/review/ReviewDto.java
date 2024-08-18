package com.chaeshin.boo.controller.dto.review;

import lombok.Builder;
import lombok.Data;

/**
 * 리뷰 수정을 위해 Controller 에서 리뷰 변경 사항을 Bind하여 Service layer로 보낼때 사용되는 DTO.
 */
@Data
@Builder
public class ReviewDto {

    private String title;
    private String body;
    private int score;
}
