package com.chaeshin.boo.service.review;

import com.chaeshin.boo.domain.LangCode;
import com.chaeshin.boo.domain.Member;
import com.chaeshin.boo.domain.restaurant.Restaurant;
import com.chaeshin.boo.domain.review.Review;
import com.chaeshin.boo.domain.review.ReviewImage;
import com.chaeshin.boo.domain.review.TranslatedReview;
import com.chaeshin.boo.repository.member.MemberRepository;
import com.chaeshin.boo.repository.restaurant.RestaurantRepository;
import com.chaeshin.boo.repository.review.ReviewRepository;
import com.chaeshin.boo.repository.review.reviewImage.ReviewImageRepository;
import com.chaeshin.boo.repository.review.translatedReview.TranslatedReviewRepository;
import com.chaeshin.boo.service.restaurant.dto.RestaurantSimpleDto;
import com.chaeshin.boo.service.review.dto.*;
import com.chaeshin.boo.utils.ResponseDto;
import com.chaeshin.boo.utils.aws.S3Service;
import com.chaeshin.boo.utils.translator.ServiceTranslatorDto;
import com.chaeshin.boo.utils.translator.Translator;
import com.chaeshin.boo.utils.translator.DeeplResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final RestaurantRepository restaurantRepository;
    private final MemberRepository memberRepository;
    private final ReviewImageRepository reviewImageRepository;
    private final TranslatedReviewRepository translatedReviewRepository;
    private final Translator translator;
    private final S3Service s3Service;


    @Override
    public ResponseDto<List<ReviewDto>> getMyReviews(Long memberId) {
        return new ResponseDto<>("나의 모든 리뷰 불러오기 성공",
                reviewRepository.findAllByMemberIdWithImage(memberId)
                        .stream().map(o -> new ReviewDto(o))
                        .collect(Collectors.toList()));
    }

    @Override
    public ResponseDto<List<ReviewDto>> getReviewByRestaurantId(Long restaurantId) {
        return new ResponseDto<>("해당 식당의 모든 리뷰 불러오기 성공",
                reviewRepository.findAllByRestaurantIdWithImage(restaurantId)
                        .stream().map(o -> new ReviewDto(o))
                        .collect(Collectors.toList()));
    }

    @Override
    public ResponseDto<RestaurantSimpleDto> getRestaurantSimpleInfoByReviewId(Long reviewId) {
        return new ResponseDto<>("리뷰의 식당 정보 반환 성공",
                new RestaurantSimpleDto(restaurantRepository
                        .findByReviewId(reviewId)));
    }

    @Override
    public ResponseDto<List<ReviewDto>> getReviewsByMemberId(Long memberId) {
        return new ResponseDto<>("해당 유저의 모든 리뷰 불러오기 성공",
                reviewRepository.findAllByMemberIdWithImage(memberId)
                        .stream().map(o -> new ReviewDto(o))
                        .collect(Collectors.toList()));
    }

    @Override
    @Transactional
    public ResponseDto<ReviewDto> createReview(Long memberId, Long restaurantId, ReviewRequestDto request) {
        Member member = memberRepository.findById(memberId).get();
        Restaurant restaurant = restaurantRepository.findById(restaurantId).get();

        DeeplResponseDto transResult = translator.requestTranslate(request.getBody(),
                LangCode.EN);
        LangCode langCode = transResult.getSourceLangCode();

        Review review = Review.builder().restaurant(restaurant)
                .member(member).title(request.getTitle())
                .body(request.getBody()).score(request.getScore())
                .langCode(langCode).build();
        Review savedReview = reviewRepository.save(review);

        /*언어 감지 시 표적 언어와 소스 언어가 달랐을 때 리뷰 번역본 함께 생성*/
        if (!langCode.equals(LangCode.EN)) {
            this.saveTranslatedReview(savedReview,
                    transResult.getTranslatedText(), LangCode.EN);
        }

        return new ResponseDto<>("리뷰 작성 성공", new ReviewDto(savedReview));
    }

    @Override
    @Transactional
    public ResponseDto updateReview(Long memberId, Long reviewId, ReviewRequestDto request) {
        Review review = reviewRepository.findById(reviewId).get();
        Member member = memberRepository.findById(memberId).get();
        if (review.getMember().getId() != member.getId()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "리뷰 작성자와 유저 불일치");
        }

        review.updateReview(request.getTitle(), request.getBody(), request.getScore());
        return new ResponseDto("리뷰 수정 성공", null);
    }

    @Override
    @Transactional
    public ResponseDto deleteReview(Long memberId, Long reviewId) {
        Review review = reviewRepository.findById(reviewId).get();
        Member member = memberRepository.findById(memberId).get();
        if (review.getMember().getId() != member.getId()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "리뷰 작성자와 유저 불일치");
        }

        review.deleteReview();
        reviewRepository.delete(review);
        return new ResponseDto("리뷰 삭제 성공", null);
    }

    @Override
    public ResponseDto<ReviewDetailDto> getReviewDetail(Long reviewId) {
        return new ResponseDto<>("리뷰 상세정보 반환 성공",
                new ReviewDetailDto(reviewRepository.findByIdWithImage(reviewId)));
    }

    @Override
    public ResponseDto<OriginalImageDto> getOriginalImageUrl(Long reviewImageId) {
        return new ResponseDto<>("이미지 원본 불러오기 성공",
                new OriginalImageDto(
                        reviewImageRepository.findById(reviewImageId).get()));
    }

    @Override
    @Transactional
    public ResponseDto<ServiceTranslatorDto> translateReview(Long memberId, Long reviewId) {
        Member member = memberRepository.findById(memberId).get();
        Review review = reviewRepository.findByIdWithTranslated(reviewId);

        LangCode sourceLang = review.getLangCode();
        LangCode targetLang = member.getLangCode();

        if (sourceLang.equals(targetLang)) {
            return new ResponseDto<>("번역 source/target 언어가 동일",null);
        }

        for (TranslatedReview tr : review.getTranslatedReviews()) {
            if (tr.getLangCode().equals(targetLang)) {
                return new ResponseDto<>("번역 성공(기존)",
                        new ServiceTranslatorDto(
                                tr.getBody(), sourceLang, targetLang));
            }
        }

        DeeplResponseDto response = translator.requestTranslate(review.getBody(), targetLang);
        TranslatedReview translatedReview = saveTranslatedReview(review, response.getTranslatedText(), targetLang);

        return new ResponseDto<>("번역 성공(신규)",
                new ServiceTranslatorDto(
                        translatedReview.getBody(), sourceLang, targetLang));

    }

    @Override
    @Transactional
    public ResponseDto<ImageUploadDto> uploadReviewImage(Long reviewId, MultipartFile image) throws IOException {
        Review review = reviewRepository.findById(reviewId).get();

        String imageUrl = s3Service.uploadFile(image);
        ReviewImage reviewImage = ReviewImage.builder()
                .review(review).imageUrl(imageUrl).build();

        reviewImageRepository.save(reviewImage);
        return new ResponseDto<>("이미지 업로드 성공",
                new ImageUploadDto(imageUrl));
    }


    private TranslatedReview saveTranslatedReview(Review review, String text, LangCode targetLang) {
        TranslatedReview translatedReview = TranslatedReview.builder()
                .review(review).body(text).langCode(targetLang).build();
        return translatedReviewRepository.save(translatedReview);
    }
}
