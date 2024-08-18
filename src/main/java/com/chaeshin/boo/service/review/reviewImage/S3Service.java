package com.chaeshin.boo.service.review.reviewImage;

import com.chaeshin.boo.exception.NoSuchReviewException;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

/**
 * Amazon S3의 endpoint 에 업로드/조회 요청을 보내고 응답을 받는 Service. <p></p>
 *  * S3Config 클래스 내에 @Bean으로 등록해두었던 S3Client 를 자동 주입 받아 사용한다.<br></br>
 *  * 따라서, Amazon S3 와의 Connection 생명 주기 또한 Bean Lifecycle 을 따른다.<br></br>
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class S3Service {
    private final S3Client s3Client; // S3Client will be @Autowired by Spring framework, since it has been registered as Spring Bean.

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucketName;


    /**
     * MultipartFile 타입 객체로 전달받은 이미지를 Amazon S3에 저장한 후 접근 경로를 Return 한다.
     * @param reviewId
     * @param image
     * @return
     * @throws IOException
     */
    @Transactional
    public String uploadImage(Long reviewId, MultipartFile image) throws IOException {
        String key = "reviews/" + reviewId + "/" + UUID.randomUUID(); // 이미지 파일 중복 해결 위해 Random UUID 를 파일 이름으로 활용.
        s3Client.putObject(PutObjectRequest.builder()
                .bucket(bucketName)
                .contentType(image.getContentType()) // Download 가 아닌 Image View 로 연결되도록 하기 위함.
                .key(key)
                .build(), RequestBody.fromInputStream(image.getInputStream(), image.getSize()));

        return key;
    }

    /**
     * ReviewImage 수정. 기존의 image를 삭제하고 새로운 image를 업로드한 후 접근 URL 반환.
     * @param imageUrl
     * @param reviewId
     * @param newImage
     * @return
     */
    @Transactional
    public String updateImage(String oldImageUrl, Long reviewId, MultipartFile newImage) throws IOException, NoSuchReviewException, SdkException {
        // 기존 사진 삭제.
        s3Client.deleteObject(DeleteObjectRequest.builder()
                                                    .bucket(bucketName)
                                                    .key(oldImageUrl)
                                                    .build());
        // 새로운 사진 업로드 및 URL 반환
        return uploadImage(reviewId, newImage);
    }



}
