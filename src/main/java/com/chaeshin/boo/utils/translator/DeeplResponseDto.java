package com.chaeshin.boo.utils.translator;

import com.chaeshin.boo.domain.LangCode;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;

@Getter
public class DeeplResponseDto {

    private LangCode sourceLangCode;
    private String translatedText;

    public DeeplResponseDto() {
    }

    public DeeplResponseDto(@JsonProperty("translations") List<HashMap<String, String>> translations) {
        this.sourceLangCode = LangCode.valueOf(translations.get(0).get("detected_source_language"));
        this.translatedText = translations.get(0).get("text");
    }
}
