package com.chaeshin.boo.controller.review;

import com.chaeshin.boo.controller.dto.BaseResponseDto;
import com.chaeshin.boo.controller.dto.DataResponseDto;
import com.chaeshin.boo.controller.dto.review.translatedReview.CreatedReviewResponse;
import com.chaeshin.boo.domain.Member;
import com.chaeshin.boo.domain.review.Review;
import com.chaeshin.boo.domain.review.TranslatedReview;
import com.chaeshin.boo.exception.SameLanguageException;
import com.chaeshin.boo.exception.auth.TokenException;
import com.chaeshin.boo.exception.deepl.DeepLException;
import com.chaeshin.boo.service.member.auth.JwtAuthService;
import com.chaeshin.boo.service.review.ReviewService;
import com.chaeshin.boo.service.review.translatedReview.TranslatedReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TranslatedReviewController {

    private final TranslatedReviewService translatedReviewService;
    private final JwtAuthService jwtAuthService;

    private final ReviewService reviewService;

    /**
     * 번역된 리뷰 생성.
     * @param auth
     * @param reviewId
     * @return ResponseEntity<BaseResponseDto>
     */
    @GetMapping("trans/{review_id}/")
    public ResponseEntity<BaseResponseDto> createTranslatedReview(@RequestHeader("Authorization") String auth,
                                                                  @PathVariable("review_id") Long reviewId){
        try{
            String accessToken = jwtAuthService.parseToken(auth);
            if(jwtAuthService.verifyAccessToken(accessToken)){
                Review review = reviewService.getReviewById(reviewId);
                Member member = review.getMember();
                TranslatedReview translatedReview = translatedReviewService.createTranslatedReview(review, member.getLangCode());
                Review updatedReview = reviewService.getReviewById(reviewId);

                CreatedReviewResponse createdReviewResponse = CreatedReviewResponse.builder()
                        .text(translatedReview.getBody())
                        .source(updatedReview.getLangCode())
                        .target(translatedReview.getLangCode())
                        .build();

                return ResponseEntity.ok().body(new DataResponseDto<>("번역된 리뷰 생성 성공", createdReviewResponse));
            }
            else{
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new BaseResponseDto("토큰이 유효하지 않습니다."));
            }
        }
        catch (TokenException e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new BaseResponseDto("토큰이 유효하지 않습니다."));
        }

        catch (DeepLException | SameLanguageException e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponseDto(e.getMessage()));
        }
    }


}
