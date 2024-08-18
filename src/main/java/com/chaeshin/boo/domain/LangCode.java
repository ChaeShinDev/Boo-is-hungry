package com.chaeshin.boo.domain;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * 회원 모국어, 작성된 리뷰의 언어, 번역 언어 코드를 위한 Enum Class
 */
public enum LangCode {
    BG, CS, DA, DE, EL, EN, ES, ET, FI, FR, HU, ID, IT,
    JA, KO, LT, LV, NB, NL, PL, PT, RO, RU, SK, SL, SV, TR, UK, ZH;


    /**
     * Response 로 들어온 String 타입의 값을 해당하는 Enum 인스턴스로 역직렬화하는 메서드.
     * @param valueFromJSon
     * @return
     */
    @JsonCreator
    private static LangCode from(String valueFromJSon){
        return Enum.valueOf(LangCode.class, valueFromJSon);
    }
}
