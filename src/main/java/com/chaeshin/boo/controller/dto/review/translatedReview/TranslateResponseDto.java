package com.chaeshin.boo.controller.dto.review.translatedReview;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Data;

/**
 * DeepL 리뷰 번역 결과인 TranslatedResultDto를 Wrap함으로써 DeepL로부터 온 응답을 Bind 하기 위한 DTO.
 * <br></br>
 * <br></br>
 *  - DeepL의 Response는 다음과 같은 구조의 JSON 데이터이다.
 * <br></br>
 *  <br></br>
 * <b><i>{"translation" : {"detected_source_language" : EN, "text" : "번역결과"}}</i></b>
 */
@Data
@Builder
public class TranslateResponseDto {
    @JsonProperty("translations")
    private List<TranslatedResultDto> translations;

    @JsonCreator
    public TranslateResponseDto(@JsonProperty("translations") List<TranslatedResultDto> translations) {
        this.translations = translations;
    }
}
