package com.chaeshin.boo.controller.review;

import com.chaeshin.boo.controller.dto.BaseResponseDto;
import com.chaeshin.boo.controller.dto.DataResponseDto;
import com.chaeshin.boo.controller.dto.member.MemberEmbeddedDto;
import com.chaeshin.boo.controller.dto.restaurant.RestaurantSimpleInfoDto;
import com.chaeshin.boo.controller.dto.review.ReviewCreatedResponseDto;
import com.chaeshin.boo.controller.dto.review.ReviewDto;
import com.chaeshin.boo.domain.Member;
import com.chaeshin.boo.domain.restaurant.Restaurant;
import com.chaeshin.boo.domain.review.Review;
import com.chaeshin.boo.domain.review.ReviewImage;
import com.chaeshin.boo.exception.auth.TokenException;
import com.chaeshin.boo.service.member.MemberService;
import com.chaeshin.boo.service.member.auth.JwtAuthService;
import com.chaeshin.boo.service.restaurant.RestaurantService;
import com.chaeshin.boo.service.review.ReviewService;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ReviewController {

    private final ReviewService reviewService;

    private final JwtAuthService jwtAuthService;

    private final RestaurantService restaurantService;

    private final MemberService memberService;


    /**
     * 신규 리뷰 작성 API
     * @param auth
     * @param restaurantId
     * @param reviewDto
     * @return
     */
    @PostMapping("review/create/{restaurant_id}/")
    public ResponseEntity<BaseResponseDto> createReview(@RequestHeader("Authorization") String auth,
                                                        @PathVariable("restaurant_id") Long restaurantId,
                                                        @RequestBody ReviewDto reviewDto){

        String accessToken = jwtAuthService.parseToken(auth);
        String msg = null;

        try{
            if(jwtAuthService.verifyAccessToken(accessToken)){
                Long memberId = jwtAuthService.getMemberIdFromToken(accessToken);
                Member relatedMember = memberService.getMemberById(memberId).get();
                Restaurant relatedRestaurant = restaurantService.getRestaurantByRestaurantId(restaurantId);

                // Builder 사용 시 생성자 내부에 정의한 양방향 연관관계 로직이 실행 안된다고 함.
                Review review = new Review(relatedMember, relatedRestaurant, reviewDto.getTitle(), reviewDto.getBody(), null, reviewDto.getScore());

                Long createdReviewId = reviewService.createReview(review);
                Review createdReview = reviewService.getReviewById(createdReviewId);
                List<ReviewImage> reviewImages = createdReview.getReviewImages();

                MemberEmbeddedDto memberDto = MemberEmbeddedDto.builder()
                        .memberId(relatedMember.getId())
                        .nickname(relatedMember.getNickname())
                        .langCode(relatedMember.getLangCode()) // Member의 선호 언어 코드.
                        .build();

                ReviewCreatedResponseDto responseDto = ReviewCreatedResponseDto.builder()
                        .reviewId(createdReviewId)
                        .memberDto(memberDto)
                        .createdAt(LocalDate.now())
                        .restaurantId(restaurantId)
                        .title(createdReview.getTitle())
                        .body(createdReview.getBody())
                        .reviewImages(reviewImages.stream()
                                .map(ReviewImage :: getImageUrl)
                                .collect(Collectors.toList()))
                        .score(createdReview.getScore())
                        .langCode(createdReview.getLangCode().toString())
                        .build();

                msg = "리뷰 작성 성공";
                return ResponseEntity.status(HttpStatus.CREATED).body(new DataResponseDto<>(msg, responseDto));
            }
            else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new BaseResponseDto());
            }

        } catch (TokenException e){
            log.error(e.getMessage());
            msg = "토큰이 유효하지 않습니다.";
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new BaseResponseDto(msg));
        }
    }

    /**
     * 리뷰 수정 API
     * @param auth
     * @param reviewDto
     * @param reviewId
     * @return
     */
    @RequestMapping(value = "review/update/{review_id}/", method = {RequestMethod.DELETE, RequestMethod.PUT})
    public ResponseEntity<BaseResponseDto> updateReview(@RequestHeader("Authorization") String auth,
                                       @RequestBody @Nullable ReviewDto reviewDto,
                                       HttpServletRequest request,
                                       @PathVariable("review_id") Long reviewId){

        try{
            String accessToken = jwtAuthService.parseToken(auth); // Parse access token from header.

            if(jwtAuthService.verifyAccessToken(accessToken)){
                Long memberId = reviewService.getReviewById(reviewId).getMember().getId(); // 리뷰 작성자의 ID(PK)

                // 리뷰 작성자의 ID == 토큰에 포함된 멤버 ID인 경우
                if(memberId.equals(jwtAuthService.getMemberIdFromToken(accessToken))){
                    String httpMethod = request.getMethod(); // Get which http method has been made for this handler method.

                    if(httpMethod.equals(HttpMethod.PUT.name())){
                        reviewService.updateReview(reviewId, reviewDto);
                        return ResponseEntity.ok().body(new BaseResponseDto("리뷰 수정 성공"));
                    }
                    else {
                        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new BaseResponseDto("리뷰 삭제 성공"));
                    }
                }
                else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponseDto("리뷰 작성자와 유저 불일치"));
                }
            }
            else{
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new BaseResponseDto("토큰이 유효하지 않습니다."));
            }
        }
        catch (TokenException e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new BaseResponseDto("토큰이 유효하지 않습니다."));
        }
        catch (HttpMessageNotReadableException e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponseDto("유효하지 않은 입력."));
        }
    }

    /**
     * 리뷰 상세 정보 반환.
     * @param auth
     * @param reviewId
     * @return
     */
    @GetMapping("review/detail/{review_id}/")
    public ResponseEntity<BaseResponseDto> getReviewDetail(@RequestHeader("Authorization") String auth,
                                                           @PathVariable("review_id") Long reviewId){

        try{
            String accessToken = jwtAuthService.parseToken(auth);
            if(jwtAuthService.verifyAccessToken(accessToken)){
                Review review = reviewService.getReviewById(reviewId);
                return ResponseEntity.ok().body(new DataResponseDto<>("리뷰 상세정보 반환 성공", review));
            }
            else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new BaseResponseDto("토큰이 유효하지 않습니다."));
            }
        }
        catch (TokenException e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new BaseResponseDto("토큰이 유효하지 않습니다."));
        }
    }

    /**
     * 회원 PK로 리뷰 전체 반환.
     * @param memberId
     * @return
     */
    @GetMapping("review/user/{user_id}/")
    public ResponseEntity<BaseResponseDto> getAllByMemberId(@PathVariable("user_id") Long memberId){

        List<Review> reviews = reviewService.getAllByMemberIdWithImage(memberId);
        return ResponseEntity.ok().body(new DataResponseDto<>("해당 유저의 리뷰 목록 반환 성공", reviews));
    }

    /**
     * 나의 리뷰 전체 조회.
     * @param auth
     * @return
     */
    @GetMapping("review/myreview/")
    public ResponseEntity<BaseResponseDto> getAllWrittenByMemberId(@RequestHeader("Authorization") String auth){

        String accessToken = jwtAuthService.parseToken(auth);

        try {
            if(jwtAuthService.verifyAccessToken(accessToken)){
                Long memberId = jwtAuthService.getMemberIdFromToken(accessToken);
                List<Review> reviews = reviewService.getAllByMemberIdWithImage(memberId);
                return ResponseEntity.ok().body(new DataResponseDto<>("나의 리뷰 목록 반환 성공", reviews));
            }
            else {
                throw new TokenException("토큰이 유효하지 않습니다.");
            }

        }
        catch (TokenException e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new BaseResponseDto("토큰이 유효하지 않습니다."));
        }
    }

    /**
     * 식당 PK로 리뷰 전체 반환.
     * @param restaurantId
     * @return
     */
    @GetMapping("review/restaurant/{restaurant_id}/")
    public ResponseEntity<BaseResponseDto> getAllByRestaurantId(@PathVariable("restaurant_id") Long restaurantId){

        List<Review> reviews = reviewService.getAllByRestaurantIdWithImage(restaurantId);

        return ResponseEntity.ok().body(new DataResponseDto<>("해당 식당의 모든 리뷰 반환 성공", reviews));
    }

    @GetMapping("review/restaurant/simpleinfo/{review_id}/")
    public ResponseEntity<BaseResponseDto> getRestaurantSimpleInfo(@RequestHeader("Authorization") String auth,
                                                                   @PathVariable("review_id") Long reviewId){

        Review review = reviewService.getReviewById(reviewId);
        Restaurant relatedRestaurant = review.getRestaurant();

        RestaurantSimpleInfoDto dto = RestaurantSimpleInfoDto.builder()
                .id(relatedRestaurant.getId())
                .name(relatedRestaurant.getName())
                .image(relatedRestaurant.getImageUrl())
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(new DataResponseDto<>("식당 간단 정보 반환 성공", dto));
    }



}
