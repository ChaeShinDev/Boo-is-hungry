package com.chaeshin.boo.service.review.dto;

import com.chaeshin.boo.domain.LangCode;
import com.chaeshin.boo.domain.review.Review;
import com.chaeshin.boo.service.member.dto.MemberInfoDto;
import com.chaeshin.boo.service.restaurant.dto.RestaurantDetailDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ReviewDetailDto {

    private Long id;
    @JsonProperty("user")
    private MemberInfoDto member;
    @JsonProperty("restaurant")
    private RestaurantDetailDto restaurant;
    private String title;
    private String body;
    private int score;
    @JsonProperty("src_lang")
    private LangCode language;
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
    @JsonProperty("review_image")
    private List<ReviewImageDto> images;

    public ReviewDetailDto(Review review) {
        this.id = review.getId();
        this.member = new MemberInfoDto(review.getMember());
        this.restaurant = new RestaurantDetailDto(review.getRestaurant());
        this.title = review.getTitle();
        this.body = review.getBody();
        this.score = review.getScore();
        this.language = review.getLangCode();
        this.createdAt = review.getCreatedAt();
        this.images = review.getReviewImages().stream()
                .map(o -> new ReviewImageDto(o)).toList();
    }
}
