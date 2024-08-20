package com.chaeshin.boo.service.review.dto;

import com.chaeshin.boo.domain.LangCode;
import com.chaeshin.boo.domain.review.Review;
import com.chaeshin.boo.service.member.dto.MemberInfoDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ReviewDto {

    private Long id;
    @JsonProperty("user")
    private MemberInfoDto member;
    @JsonProperty("restaurant")
    private Long restaurantId;
    private String title;
    private String body;
    private int score;
    @JsonProperty("src_lang")
    private LangCode language;
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
    @JsonProperty("image")
    private List<ReviewImageDto> images;

    public ReviewDto(Review review) {
        this.id = review.getId();
        this.restaurantId = review.getRestaurant().getId();
        this.title = review.getTitle();
        this.body = review.getBody();
        this.score = review.getScore();
        this.language = review.getLangCode();
        this.createdAt = review.getCreatedAt();
        this.member = new MemberInfoDto(review.getMember());
        this.images = review.getReviewImages().stream()
                .map(o -> new ReviewImageDto(o)).toList();
    }
}
