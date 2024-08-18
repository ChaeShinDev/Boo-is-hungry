package com.chaeshin.boo.service.review.translatedReview;

import com.chaeshin.boo.domain.LangCode;
import com.chaeshin.boo.domain.review.Review;
import com.chaeshin.boo.domain.review.TranslatedReview;
import com.chaeshin.boo.exception.SameLanguageException;
import com.chaeshin.boo.exception.deepl.DeepLException;
import com.chaeshin.boo.repository.review.ReviewRepository;
import com.chaeshin.boo.repository.review.translatedReview.TranslatedReviewRepository;
import com.chaeshin.boo.service.review.ReviewService;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TranslatedReviewService {

    private final TranslatedReviewRepository translatedReviewRepository;
    private final TranslationService translationService;
    private final ReviewRepository reviewRepository; // ReviewService 와 TranslatedReviewService 간 Circular Dependency 발생 차단 위함.

    /**
     * 리뷰 번역 단 건 조회
     * @param translatedReviewId
     * @return
     */

    public TranslatedReview getTranslatedReviewById(Long translatedReviewId){
        Optional<TranslatedReview> found = translatedReviewRepository.findById(translatedReviewId);

        // 리뷰 번역 존재할 경우.
        if(found.isPresent()){
            return found.get();
        }
        // 리뷰 번역 존재하지 않을 경우.
        else {
            throw new NoSuchElementException("해당 리뷰 번역이 존재하지 않습니다.");
        }

    }

    /**
     * 리뷰 ID로 해당 리뷰의 번역 전체 조회.
     * @param reviewId
     * @return
     */
    public List<TranslatedReview> getAllTranslatedReview(Long reviewId){
        return translatedReviewRepository.findAllByReviewId(reviewId);
    }

    /**
     * 리뷰를 받아 리뷰 번역 생성.
     * @param review
     * @return
     * @throws DeepLException DeepL API 호출 중 발생하는 예외.
     * @throws SameLanguageException 번역 대상 언어와 리뷰 작성 언어가 같을 경우 발생하는 예외.
     */
    @Transactional
    public TranslatedReview createTranslatedReview(Review review, LangCode reviewWriterLangCode) throws DeepLException, SameLanguageException {
            
        LangCode targetLang = reviewWriterLangCode; // 코드 가독성을 위해 선언.
        // TranslatedReview 엔티티 생성.
        TranslatedReview translatedReview = new TranslatedReview(review.getBody(), targetLang, review);

        // 번역 보내고 다시 받아 저장.
        return translatedReviewRepository.save(translationService.translate(translatedReview));
    }
}
