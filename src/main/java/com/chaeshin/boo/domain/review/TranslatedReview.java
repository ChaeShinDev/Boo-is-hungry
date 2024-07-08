package com.chaeshin.boo.domain.review;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor
public class TranslatedReview {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "translated_review_id")
    private Long id;

    private String body; // 번역된 본문
    private String bodyLang; // 번역된 언어 코드

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @NotNull
    @JoinColumn(name = "review_id")
    private Review review;

    // 생성자 메서드
    @Builder
    public TranslatedReview(String body, String bodyLang, Review review) {
        this.body = body;
        this.bodyLang = bodyLang;
        this.review = review;

        // 양방향 연관관계 맺어주기
        review.getTranslatedReviews().add(this);
    }

    // 편의 기능 메서드
    public void updateBody(String newBody){this.body = newBody;}
}
