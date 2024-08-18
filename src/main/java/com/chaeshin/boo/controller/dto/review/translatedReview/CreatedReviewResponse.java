package com.chaeshin.boo.controller.dto.review.translatedReview;

import com.chaeshin.boo.domain.LangCode;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreatedReviewResponse {

    @JsonProperty("text")
    private String text;

    @JsonProperty("source")
    private LangCode source;

    @JsonProperty("target")
    private LangCode target;

}
