package com.chaeshin.boo.utils.translator;

import com.chaeshin.boo.domain.LangCode;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class DeeplRequestDto {

    private List<String> text;
    @JsonProperty("target_lang")
    private LangCode targetLang;

    public DeeplRequestDto(String text, LangCode langCode) {
        this.text = new ArrayList<>();
        this.text.add(text);
        this.targetLang = langCode;
    }
}
