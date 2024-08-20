package com.chaeshin.boo.service.review;

import com.chaeshin.boo.service.restaurant.dto.RestaurantSimpleDto;
import com.chaeshin.boo.service.review.dto.*;
import com.chaeshin.boo.utils.ResponseDto;
import com.chaeshin.boo.utils.translator.ServiceTranslatorDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ReviewService {

    /*나의 모든 리뷰 조희*/
    ResponseDto<List<ReviewDto>> getMyReviews(Long memberId);

    /*식당 ID로 모든 리뷰 조회*/
    ResponseDto<List<ReviewDto>> getReviewByRestaurantId(Long restaurantId);

    /*해당 리뷰의 간단한 식당 정보 조회*/
    ResponseDto<RestaurantSimpleDto> getRestaurantSimpleInfoByReviewId(Long reviewId);

    /*특정 유저의 모든 리뷰 조회*/
    ResponseDto<List<ReviewDto>> getReviewsByMemberId(Long memberId);

    /*리뷰 작성*/
    ResponseDto<ReviewDto> createReview(Long memberId, Long restaurantId, ReviewRequestDto reviewRequestDto);

    /*리뷰 수정*/
    ResponseDto updateReview(Long memberId, Long reviewId, ReviewRequestDto reviewRequestDto);

    /*리뷰 삭제*/
    ResponseDto deleteReview(Long memberId, Long reviewId);

    /*리뷰 상세정보 불러오기*/
    ResponseDto<ReviewDetailDto> getReviewDetail(Long reviewId);

    /*리뷰 이미지 원본 조회*/
    ResponseDto<OriginalImageDto> getOriginalImageUrl(Long reviewImageId);

    /*번역본 유무 확인 및 리뷰 번역*/
    ResponseDto<ServiceTranslatorDto> translateReview(Long memberId, Long reviewId);

    /*리뷰 이미지 업로드*/
    ResponseDto<ImageUploadDto> uploadReviewImage(Long reviewId, MultipartFile image) throws IOException;
}
