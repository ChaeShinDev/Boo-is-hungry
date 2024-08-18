package com.chaeshin.boo.service.review.translatedReview;


import com.chaeshin.boo.controller.dto.review.translatedReview.TranslateRequestDto;
import com.chaeshin.boo.controller.dto.review.translatedReview.TranslateResponseDto;
import com.chaeshin.boo.controller.dto.review.translatedReview.TranslatedResultDto;
import com.chaeshin.boo.domain.review.Review;
import com.chaeshin.boo.domain.review.TranslatedReview;
import com.chaeshin.boo.exception.SameLanguageException;
import com.chaeshin.boo.exception.deepl.DeepLException;
import com.chaeshin.boo.exception.deepl.QuotaExceededException;
import com.chaeshin.boo.exception.deepl.TooManyRequestsException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * DeepL API 에 리뷰의 번역을 요청하고 결과를 받아 애플리케이션으로 제공하는 Utility 성격의 Component.
 */
@Component
@RequiredArgsConstructor
public class TranslationService {

    @Value("${deepl.apiKey}")
    private String apiKey;
    private final String tokenPrefix = "DeepL-Auth-Key ";
    private final String baseUrl = "https://api-free.deepl.com/v2/translate";

    /**
     * 번역된 리뷰를 받아 번역 요청을 보내고 결과를 받아 번역된 리뷰를 반환.
     * @param translatedReview
     * @return TranslatedReview
     * @Throws DeepLException
     */
     TranslatedReview translate(TranslatedReview translatedReview) throws DeepLException {

         WebClient webClient = WebClient.create(baseUrl);

         // TranslatedReview -> TranslateRequestDto 생성.
         TranslateRequestDto requestDto = TranslateRequestDto.builder()
                 .text(new String[]{translatedReview.getBody()}) // 원문
                 .targetLangCode(translatedReview.getLangCode().toString()) // targetLang
                 .build();

         // DeepL API 로 요청 전송 후 응답 수신.
         // 응답은 TranslateResponseDto로 Bind 된다.
         // 이때 Response 내에선 String 으로 표현되는 LangCode 는,
         //  @JsonCreator 로 LangCode 클래스 내에 명시된 메서드를 통해 역직렬화 되어 Enum Type 필드에 Bind 된다.
         TranslateResponseDto responseDto = webClient.post()
                 .header("Authorization", tokenPrefix + apiKey)
                 .body(BodyInserters.fromValue(requestDto))
                 .retrieve()
                 .onStatus(HttpStatus.TOO_MANY_REQUESTS::equals,
                         response -> Mono.error(new TooManyRequestsException("api 사용량 초과")))
                 .onStatus(HttpStatus.PAYMENT_REQUIRED::equals,
                         response -> Mono.error(new QuotaExceededException("텍스트 용량 초과")))
                 .bodyToMono(TranslateResponseDto.class) // Throws WebClientException.
                 .block();

         TranslatedResultDto translation = responseDto.getTranslations()
                 .get(0); // responseDto == null 이 되기 전에 예외가 발생하므로 경고 무시.
         Review relatedReview = translatedReview.getReview(); // Review 의 LangCode 초기화 상태를 확인하기 위해 꺼내기.

         // Case I : 탐지된 원문 언어 != 결과 언어일 경우에만 이후의 프로세스 진행.
         if (translation.getDetectedSourceLang() != translatedReview.getLangCode()) {

             /* 해당 리뷰가 이미 번역된 적 있는 리뷰라면  LangCode 가 존재하고
              * 방금 처음 만들어져 LangCode 파악 위해 보내진 Review라면 해당 필드엔 데이터 존재 하지 않는다.
              * 만약 해당 Review 의 LangCode가 초기화되지 않은 상태라면 Review.initLangCode(LangCode lang)을 통해 초기화 해주기.
              */
             if (relatedReview.getLangCode() == null) {
                 relatedReview.initLangCode(translation.getDetectedSourceLang());
             }

             translatedReview.updateBody(
                     translation.getText()); // 인자로 받은 영속성 엔티티인 translatedReview의 본문을 번역된 텍스트로 갱신.

             return translatedReview;
         }

         else {
             if (relatedReview.getLangCode() == null) {relatedReview.initLangCode(translation.getDetectedSourceLang());}
             throw new SameLanguageException("번역 source/target 언어가 동일"); // 400
         }
     }
}
