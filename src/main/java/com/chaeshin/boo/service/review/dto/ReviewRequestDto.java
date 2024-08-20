package com.chaeshin.boo.service.review.dto;

import lombok.Getter;

@Getter
public class ReviewRequestDto {

    private String title;
    private String body;
    private int score;

    public ReviewRequestDto(String title, String body, int score) {
        this.title = title;
        this.body = body;
        this.score = score;
    }
}
