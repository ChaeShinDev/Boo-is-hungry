package com.chaeshin.boo.service.review.reviewImage;

import com.chaeshin.boo.domain.review.Review;
import com.chaeshin.boo.domain.review.ReviewImage;
import com.chaeshin.boo.exception.NoSuchReviewException;
import com.chaeshin.boo.repository.review.ReviewRepository;
import com.chaeshin.boo.repository.review.reviewImage.ReviewImageRepository;
import jakarta.validation.Valid;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Optional;
import javax.imageio.ImageIO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ReviewImageService {

    private final S3Service s3Service;
    private final ReviewImageRepository reviewImageRepository;
    private final ReviewRepository reviewRepository;

    /**
     * ReviewImageController 로부터 전달받은 이미지에 대해 다음의 두 가지 작업을 수행한다.
     * <p></p>
     * 1) S3Service를 호출해 이미지를 저장하고 URL을 반환받는다.
     * <br></br>
     * 2) 반환받은 URL과 reviewId를 통해 조회한 Review 엔티티를 인자로 활용하여 ReviewImage 엔티티 생성 후 DB에 저장한다.
     * <p></p>
     * <i>이미지가 없는 경우도 존재한다.</i> 해당 메서드는 <b>Controller에서 이미지 존재 여부 판단 후 존재할 때에만 호출 된다</b>.
     * <p></p>
     * 만약 해당 ReviewImage와 관련된 Review가 존재하지 않을 경우 NoSuchReviewException 을 발생시킨다.
     * @param reviewId
     * @param image
     * @return
     * @throws NoSuchReviewException
     */
    @Transactional
    public ReviewImage saveReviewImage(@Valid Long reviewId, MultipartFile image) throws IOException, NoSuchReviewException {
        Optional<Review> relatedReview = reviewRepository.findById(reviewId);

        String originalUrl = s3Service.uploadImage(reviewId, image);
        if(relatedReview.isPresent()){
            ReviewImage newReviewImage = ReviewImage.builder()
                                                        .imageUrl(originalUrl)
                                                        .review(relatedReview.get())
                                                        .build();
            return reviewImageRepository.save(newReviewImage);
        } else {
            throw new NoSuchReviewException("해당 Review 가 존재하지 않습니다.");
        }
    }

    /**
     * ReviewImage 조회.
     * @param reviewImageId
     * @return
     */

    public ReviewImage findReviewImage(Long reviewImageId) throws NoSuchElementException {
        Optional<ReviewImage> found =  reviewImageRepository.findById(reviewImageId);
        if(found.isPresent()){
            return found.get();
        }
        else{
            throw new NoSuchElementException("해당 ReviewImage 가 이미 삭제되었거나 존재하지 않습니다.");
        }
    }

    /**
     * ReviewImage 이미지 수정
     * @param reviewImageId
     * @param newImage
     * @throws NoSuchElementException - 해당 리뷰가 존재하지 않을 때 발생.
     * @throws IOException - 해당 이미지의 썸네일을 만드는 과정에서 MultipartFile 의 InputStream을 읽는 과정에서 문제가 발생한 경우.
     */
    @Transactional
    public void updateReviewImage(Long reviewImageId, MultipartFile newImage) throws NoSuchElementException, IOException {
        Optional<ReviewImage> foundReviewImage = reviewImageRepository.findById(reviewImageId); // ReviewImage 조회.
        if(foundReviewImage.isPresent()){
            ReviewImage targetReviewImage = foundReviewImage.get(); // 수정 대상 ReviewImage.
            Long reviewId = targetReviewImage.getReview().getId(); // 연관된 Review 엔티티 ID 조회.

            String newImageUrl = s3Service.updateImage(targetReviewImage.getImageUrl(), reviewId, newImage);
            targetReviewImage.updateReviewImage(newImageUrl);


        } else {
            throw new NoSuchElementException("해당 ReviewImage 가 이미 삭제되었거나 존재하지 않습니다.");
        }
    }

    /**
     * ReviewImage 단 건 삭제
     * <p></p>
     * <b>주의 사항</b>
     * JpaRepository.deleteById(ID id)는 해당 id가 null일 시 IllegalArgumentException을 발생시키나,
     * <br></br>
     * 해당 id에 해당하는 Entity가 DB에 존재하지 않을 시 조용히 처리된다(어떤 예외도 발생시키지 않는다).
     * <br></br>
     * 이는 어차피 해당 Entity가 없기 때문에 삭제를 한 것이나 다름이 없기 때문으로 추정된다.
     *
     * @param reviewImageId
     */
    @Transactional
    public void deleteReviewImage(Long reviewImageId) {
        reviewImageRepository.deleteById(reviewImageId);
    }

    /**
     * ReviewImage 전체 삭제
     */
    @Transactional
    public void deleteAll(){
        reviewImageRepository.deleteAll();
    }


    /**
     * Controller로부터 전달받은 이미지의 Preview(Thumbnail) 이미지를 생성해주는 Utility method.
     * <p></p>
     * <b>Plain Java 를 활용해 구현한 버전 : 공부 목적!</b>
     * @param originalImage
     */
    private MultipartFile createThumbnailViaPlainJava(MultipartFile originalImage) throws IOException {

        String originalName = originalImage.getOriginalFilename();

        // 1. javax.ImageIO은 MultipartFile 타입의 이미지의 InputStream을 읽고 해당 이미지의 decode된 형태인 BufferedImage로 반환.
        BufferedImage original = ImageIO.read(originalImage.getInputStream());

        // 2. calculateThumbnailSize()를 호출한다.
        int[] thumbnailSize = calculateThumbnailSize(original.getWidth(), original.getHeight());

        // 3. 축소된 이미지를 Image형 오브젝트로 생성한다.
        Image resized = original.getScaledInstance(thumbnailSize[0], thumbnailSize[1], Image.SCALE_SMOOTH);

        // 4. Image 형의 resized를 담기 위해 resized와 같은 크기의 BufferedImage 객체를 인스턴스화 한다. 도화지를 만드는 과정
        BufferedImage output = new BufferedImage(thumbnailSize[0], thumbnailSize[1], BufferedImage.TYPE_INT_RGB);

        // 5. 도화지에 칠하기
        output.getGraphics().drawImage(resized, 0, 0, null);

        File resultImage = new File(originalName + "_resized.jpg");

        ImageIO.write(output, "jpg", resultImage);


        return new CustomMultipartFile(resultImage);
    }

    /**
     * 원본 이미지의 가로, 세로로부터 Thumbnail 이미지의 크기를 계산하여 반환하는 메서드.
     * 현재 기능 요구상 Preview(Thumbnail)의 크기는 800 * 600 을 넘지 않아야 한다.
     * <br></br>
     * 이를 위해 원본의 비율을 유지하되, 800*600을 넘지 않는 선에서 가장 큰 가로 & 세로 길이를 반환하도록 설계한다.
     * @param originalWidth
     * @param origianlHeight
     * @return
     */
    private int[] calculateThumbnailSize(int originalWidth, int originalHeight){
        // 1. 원본 가로와 세로 비율 구하기.
        float originalRatio = (float) (originalWidth / originalHeight);

        // 2. 목표 축소 비율을 계산한 후 가로와 세로 양쪽에 모두 적용하여 계산 결과를 도출한다.
        int smaller = Math.min(originalWidth, originalHeight);
        double reductionRatio = (double) 600 / smaller;

        int reducedWidth = (int) (originalWidth * reductionRatio);
        int reducedHeight = (int) (originalHeight * reductionRatio);

        // 3. 모든 길이가 800 600 내로 들어오도록 마지막으로 조정한다. 큰 쪽이 800이내로 돌아오도록 조정하는 것이 목적.
        int largerLengthGap = (smaller == originalWidth) ? (originalHeight - 800) : (originalWidth - 800);
        double finalReductionRatio = 0;

        if(largerLengthGap > 0){
            if(smaller == originalWidth){
                finalReductionRatio = (double) 800 / originalHeight;
            } else {
                finalReductionRatio = (double) 800 / originalWidth;
            }
            reducedWidth = (int) (reducedWidth * finalReductionRatio);
            reducedHeight = (int) (reducedHeight * finalReductionRatio);
        }

        return new int[]{reducedWidth, reducedHeight};
    }
}
