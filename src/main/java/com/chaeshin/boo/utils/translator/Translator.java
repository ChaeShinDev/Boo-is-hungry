package com.chaeshin.boo.utils.translator;

import com.chaeshin.boo.domain.LangCode;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class Translator {

    static private String appKey;
    static private WebClient webClient;

    public Translator(@Value("${deepl.apiKey}") String appKey) {
        this.appKey = appKey;
    }

    @PostConstruct
    public void init() {
        webClient = WebClient.create("https://api-free.deepl.com/v2/translate");
    }


    public DeeplResponseDto requestTranslate(String text, LangCode targetLangCode) {
        return webClient.post()
                .body(BodyInserters.fromValue(new DeeplRequestDto(text, targetLangCode)))
                .header("Authorization", "DeepL-Auth-Key " + appKey)
                .retrieve().bodyToMono(DeeplResponseDto.class).block();
    }
}
