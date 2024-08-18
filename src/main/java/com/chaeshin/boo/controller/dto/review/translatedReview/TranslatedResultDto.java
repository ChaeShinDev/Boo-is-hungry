package com.chaeshin.boo.controller.dto.review.translatedReview;

import com.chaeshin.boo.domain.LangCode;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

/**
 * 번역 결과 자체를 담는 DTO.
 */
@Data
@Builder
public class TranslatedResultDto {

    @JsonProperty("detected_source_language")
    private LangCode detectedSourceLang; // 자동 감지된 원문 언어의 코드.

    @JsonProperty("text")
    private String text; // 번역 결과 텍스트.
}
