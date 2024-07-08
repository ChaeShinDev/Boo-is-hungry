package com.chaeshin.boo.domain.review;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@Getter
@NoArgsConstructor
public class ReviewImage {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_image_id")
    private Long id;

    private String imageUrl;

    @NotNull // @NotNull 제약 추가.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    @Builder
    public ReviewImage(@NonNull String imageUrl, @NonNull Review review) {
        this.imageUrl = imageUrl;
        this.review = review;

        // 양방향 연관관계 맺어주기
        review.getReviewImages().add(this);
    }

    // 편의 기능 메서드
    /**
     * ReviewImage 이미지 변경
     * @param newImageUrl
     */
    public void updateReviewImage(String newImageUrl){this.imageUrl = newImageUrl;}
}
