package com.chaeshin.boo.domain.review;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
public class ReviewImage {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_image_id")
    private Long id;

    private String imageUrl;

    @NotNull // @NotNull 제약 추가.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    // 편의 기능 메서드

    /**
     * ReviewImage 이미지 변경
     * @param newImageUrl
     */
    public void updateReviewImage(String newImageUrl){this.imageUrl = newImageUrl;}

    /**
     * 연관 관계를 고려한 특수 목적 생성자
     * <br></br>
     * 리뷰 이미지 엔티티 생성 시 관련있는 Review 엔티티의 reviewImages 필드에 해당 ReviewImage 추가하고 생성 및 반환
     * @param review
     * @return
     */
    public static ReviewImage createReviewImage(Review review) {
        ReviewImage reviewImage = new ReviewImage();
        reviewImage.review = review;
        review.getReviewImages().add(reviewImage);

        return reviewImage;
    }
}
