package com.chaeshin.boo.controller.dto.review.translatedReview;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

/**
 * DeepL API 로 리뷰 번역 요청을 보낼 때 사용되는 DTO.
 */
@Data
@Builder
public class TranslateRequestDto {

    @JsonProperty("text")
    private String[] text; // 번역할 Review의 Body.

    @JsonProperty("target_lang")
    private String targetLangCode; // 번역 결과 언어 코드.
}
