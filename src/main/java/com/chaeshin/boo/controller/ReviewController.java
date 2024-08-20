package com.chaeshin.boo.controller;

import com.chaeshin.boo.exception.ExpiredTokenException;
import com.chaeshin.boo.exception.TokenNotFoundException;
import com.chaeshin.boo.service.restaurant.dto.RestaurantSimpleDto;
import com.chaeshin.boo.service.review.ReviewService;
import com.chaeshin.boo.service.review.dto.*;
import com.chaeshin.boo.utils.ResponseDto;
import com.chaeshin.boo.utils.jwt.JwtProvider;
import com.chaeshin.boo.utils.translator.ServiceTranslatorDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;
    private final JwtProvider jwtProvider;

    @GetMapping("/myreview/")
    public ResponseEntity<ResponseDto<List<ReviewDto>>> getMyReview(HttpServletRequest request) {
        try {jwtProvider.validateToken(request);}
        catch (ExpiredTokenException e) {throw e;}
        catch (TokenNotFoundException e) {throw e;}
        try {
            Long memberId = jwtProvider.getMemberId(request);
            ResponseDto<List<ReviewDto>> result = reviewService.getMyReviews(memberId);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/restaurant/{restaurantId}/")
    public ResponseEntity<ResponseDto<List<ReviewDto>>> getRestaurantByReview(
            HttpServletRequest request, @PathVariable Long restaurantId) {
        try {jwtProvider.validateToken(request);}
        catch (ExpiredTokenException e) {throw e;}
        catch (TokenNotFoundException e) {throw e;}

        try {
            ResponseDto<List<ReviewDto>> result = reviewService
                    .getReviewByRestaurantId(restaurantId);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/restaurant/simpleinfo/{reviewId}/")
    public ResponseEntity<ResponseDto<RestaurantSimpleDto>> getRestaurantSimpleInfo(
            HttpServletRequest request, @PathVariable Long reviewId) {
        try {jwtProvider.validateToken(request);}
        catch (ExpiredTokenException e) {throw e;}
        catch (TokenNotFoundException e) {throw e;}

        try{
            ResponseDto<RestaurantSimpleDto> result = reviewService
                    .getRestaurantSimpleInfoByReviewId(reviewId);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/user/{memberId}/")
    public ResponseEntity<ResponseDto<List<ReviewDto>>> getMemberReviews
            (HttpServletRequest request, @PathVariable Long memberId) {
        try {jwtProvider.validateToken(request);}
        catch (ExpiredTokenException e) {throw e;}
        catch (TokenNotFoundException e) {throw e;}

        try {
            ResponseDto<List<ReviewDto>> result = reviewService
                    .getReviewsByMemberId(memberId);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/create/{restaurantId}/")
    public ResponseEntity<ResponseDto<ReviewDto>> createReview(
            HttpServletRequest request, @PathVariable Long restaurantId,
            @RequestBody ReviewRequestDto reviewRequestDto) {
        try {jwtProvider.validateToken(request);}
        catch (ExpiredTokenException e) {throw e;}
        catch (TokenNotFoundException e) {throw e;}

        try {
            Long memberId = jwtProvider.getMemberId(request);
            ResponseDto<ReviewDto> result = reviewService.createReview(memberId,
                    restaurantId, reviewRequestDto);
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        } catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/update/{reviewId}/", method = {RequestMethod.DELETE, RequestMethod.PUT})
    public ResponseEntity<ResponseDto> updateDeleteReview(
            HttpServletRequest request, @PathVariable Long reviewId,
            @RequestBody(required = false) ReviewRequestDto reviewRequestDto) {
        try {jwtProvider.validateToken(request);}
        catch (ExpiredTokenException e) {throw e;}
        catch (TokenNotFoundException e) {throw e;}

        Long memberId = jwtProvider.getMemberId(request);
        try {
            if ("PUT".equals(request.getMethod())) {
                ResponseDto result = reviewService.updateReview(memberId, reviewId, reviewRequestDto);
                return new ResponseEntity<>(result, HttpStatus.OK);
            } else if ("DELETE".equals(request.getMethod())) {
                ResponseDto result = reviewService.deleteReview(memberId, reviewId);
                return new ResponseEntity<>(result, HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(null, HttpStatus.METHOD_NOT_ALLOWED);
            }
        } catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/detail/{reviewId}/")
    public ResponseEntity<ResponseDto<ReviewDetailDto>> getReviewDetail(HttpServletRequest request, @PathVariable Long reviewId) {
        try {jwtProvider.validateToken(request);}
        catch (ExpiredTokenException e) {throw e;}
        catch (TokenNotFoundException e) {throw e;}

        try {
            ResponseDto<ReviewDetailDto> result = reviewService.getReviewDetail(reviewId);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/image/original/{imageId}/")
    public ResponseEntity<ResponseDto<OriginalImageDto>> getOriginalImage(@PathVariable Long imageId) {
        try {
            return new ResponseEntity<>(
                    reviewService.getOriginalImageUrl(imageId),
                    HttpStatus.OK
            );
        } catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/trans/{reviewId}/")
    public ResponseEntity<ResponseDto<ServiceTranslatorDto>> translateReview(
            HttpServletRequest request, @PathVariable Long reviewId) {
        try {jwtProvider.validateToken(request);}
        catch (ExpiredTokenException e) {throw e;}
        catch (TokenNotFoundException e) {throw e;}

        Long memberId = jwtProvider.getMemberId(request);

        try {
            return new ResponseEntity<>(
                    reviewService.translateReview(memberId, reviewId),
                    HttpStatus.OK
            );
        } catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/image/upload/{reviewId}/")
    public ResponseEntity<ResponseDto<ImageUploadDto>> uploadImage (
            @PathVariable Long reviewId,@RequestParam(name = "original_image") MultipartFile image) {
        try {
            return new ResponseEntity<>(
                    reviewService.uploadReviewImage(reviewId, image),
                    HttpStatus.CREATED
            );
        } catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
