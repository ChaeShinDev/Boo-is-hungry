package com.chaeshin.boo.domain.review;

import com.chaeshin.boo.domain.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

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
}
