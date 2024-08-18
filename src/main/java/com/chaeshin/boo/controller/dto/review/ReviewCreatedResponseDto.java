package com.chaeshin.boo.controller.dto.review;

import com.chaeshin.boo.controller.dto.member.MemberEmbeddedDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReviewCreatedResponseDto {

    @JsonProperty("id")
    private Long reviewId;

    @JsonProperty("image")
    private List<String> reviewImages;

    @JsonProperty("user")
    private MemberEmbeddedDto memberDto;

    @JsonProperty("title")
    private String title;

    @JsonProperty("body")
    private String body;

    @JsonProperty("src_lang")
    private String langCode;

    @JsonProperty("created_at")
    private LocalDate createdAt;

    @JsonProperty("score")
    private int score;

    @JsonProperty("restaurant")
    private Long restaurantId;

}