package com.chaeshin.boo.utils.translator;

import com.chaeshin.boo.domain.LangCode;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class ServiceTranslatorDto {

    private String text;
    @JsonProperty("source")
    private LangCode sourceLang;
    @JsonProperty("target")
    private LangCode targetLang;

    public ServiceTranslatorDto(String text, LangCode sourceLang, LangCode targetLang) {
        this.text = text;
        this.sourceLang = sourceLang;
        this.targetLang = targetLang;
    }
}
