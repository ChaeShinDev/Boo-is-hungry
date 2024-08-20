package com.chaeshin.boo.utils.geocoding;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class GeoCoding {

    static private String appKey;
    static private WebClient webClient;

    public GeoCoding(@Value("${kakao.appKey}") String appKey) {
        this.appKey = appKey;
    }

    @PostConstruct
    public void init() {
        webClient = WebClient.create("https://dapi.kakao.com/v2/local/search/address.json");
    }

    public CoordinateDto geoCode(String address) {
        GeoCodingResponseDto responseDto = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("query", address)
                        .build())
                .header("Authorization", "KakaoAK " + this.appKey)
                .retrieve().bodyToMono(GeoCodingResponseDto.class).block();

        CoordinateDto result =  responseDto.toCoordinateDto();
        for (int i = 0; i < 100; i++) {
            System.out.println(result.getLatitude());
            System.out.println(result.getLongitude());
        }
        return result;
    }
}
