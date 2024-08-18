package com.chaeshin.boo.controller.review.reviewImage;

import com.chaeshin.boo.controller.dto.BaseResponseDto;
import com.chaeshin.boo.controller.dto.CreatedReviewImageDto;
import com.chaeshin.boo.controller.dto.DataResponseDto;
import com.chaeshin.boo.domain.review.ReviewImage;
import com.chaeshin.boo.exception.NoSuchReviewException;
import com.chaeshin.boo.exception.auth.TokenException;
import com.chaeshin.boo.service.member.auth.JwtAuthService;
import com.chaeshin.boo.service.review.reviewImage.ReviewImageService;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ReviewImageController {

    private final ReviewImageService reviewImageService;
    private final JwtAuthService jwtAuthService;

    private String msg; // Error 내용을 담을 String 객체. 두 가지 catch 분기에서 모두 공통적으로 사용하기에 scope 가 넓도록 위치를 정하였다.

    @PostMapping("review/image/upload/{review_id}/")
    public ResponseEntity<BaseResponseDto> uploadReviewImage(@RequestPart("original_image")MultipartFile image,
                                                             @PathVariable("review_id") Long reviewId){

        try{
            ReviewImage createdReviewImage = reviewImageService.saveReviewImage(reviewId, image);
            CreatedReviewImageDto createdReviewImageDto = new CreatedReviewImageDto();

            createdReviewImageDto.setMsg("이미지 업로드 성공");
            createdReviewImageDto.setOriginal_image(createdReviewImage.getImageUrl());
            createdReviewImageDto.setReview_image(createdReviewImage.getImageUrl());

            return ResponseEntity.ok().body(createdReviewImageDto);

        }

        // 예외 1 - NoSuchReviewException e : PathVariable 로 주어진 review_id를 id로 가진 Review 가 존재하지 않는 상황.
        catch (NoSuchReviewException e){
            // e.printStackTrace(); : "Thread starvation or clock lead detected" 로그 발견 가능. 잠시 주석 처리.
            log.error(e.getMessage());

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BaseResponseDto(e.getMessage())); // Code : 404
        }

        // 예외 2 - IOException e : 파일의 크기가 너무 크거나(FileSizeLimitExceededException), S3Service.uploadImage() 내 getInputStream()에 문제가 발생한 경우
        catch (IOException e){
            // FileSizeLimitExceededException 이 IOException 을 상속하기 때문에 IOException 만으로 핸들링 가능.
            // e.printStackTrace();
            msg = "사진 용량이 너무 크거나, 사진이 올바르지 않습니다! 5MB 미만의 사진만 첨부 가능합니다.";
            log.error(msg);

            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new BaseResponseDto()); // Code : 422
        }
    }

    @GetMapping("review/image/original/{image_id}/")
    public ResponseEntity<BaseResponseDto> findOriginalReviewImage(@PathVariable("image_id") Long reviewImageId){

        try{
            String originalImageUrl = reviewImageService.findReviewImage(reviewImageId).getImageUrl();
            msg = "이미지 원본 불러오기 성공";
            DataResponseDto<String> drd = new DataResponseDto<>(msg, originalImageUrl);

            return ResponseEntity.ok().body(drd);
        }
        // 예외 - NoSuchElementException : 해당 ReviewImage 가 존재하지 않을 때 발생.
        catch (NoSuchElementException e){
            e.printStackTrace();
            log.error(e.getMessage());

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BaseResponseDto(e.getMessage()));
        }
    }

    /**
     * 리뷰 이미지 수정
     * @param reviewImageId
     * @param newImage
     * @return
     */
    @RequestMapping(value = "review/image/update/{image_id}/",
                    method = {RequestMethod.DELETE, RequestMethod.PUT})
    public ResponseEntity<BaseResponseDto> updateImage(@PathVariable("image_id") Long reviewImageId,
                                                       @RequestHeader("Authorization") String auth,
                                                       @Nullable @RequestPart("original_image") MultipartFile newImage,
                                                       HttpServletRequest request){
        String msg = null;
        try{
            String accessToken = jwtAuthService.parseToken(auth);
            // Verify the given access token.
            if(jwtAuthService.verifyAccessToken(accessToken)) {
                Long memberId = jwtAuthService.getMemberIdFromToken(accessToken); // Parse & Retrieve member ID from given access token.

                ReviewImage reviewImage = reviewImageService.findReviewImage(reviewImageId);
                Long reviewWriterId = reviewImage.getReview().getMember().getId();

                if(memberId.equals(reviewWriterId)){
                    String httpMethod = request.getMethod(); // Get which http method has been made for this handler method.

                    // PUT 요청인 경우.
                    if(httpMethod.equals("PUT")){
                        msg = "사진 수정 성공";
                        reviewImageService.updateReviewImage(reviewImageId, newImage);
                        return ResponseEntity.ok().body(new BaseResponseDto(msg));
                    }
                    // DELETE 요청인 경우.
                    else {
                        msg = "사진 삭제 성공";
                        reviewImageService.deleteReviewImage(reviewImageId);
                        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new BaseResponseDto(msg));
                    }
                }
                else {
                    msg = "리뷰 작성자와 유저 불일치";
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponseDto(msg));
                }
            }
            else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new BaseResponseDto("UNAUTHORIZED"));
            }

        } catch (TokenException te){
            log.error(te.getMessage());
            msg = "토큰이 올바르지 않습니다.";
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new BaseResponseDto(msg));
        } catch (MultipartException | IOException e){
            msg = "유효하지 않은 파일";
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponseDto(msg));
        }
    }

}
