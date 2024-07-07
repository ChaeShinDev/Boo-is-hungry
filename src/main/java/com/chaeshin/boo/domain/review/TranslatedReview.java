package com.chaeshin.boo.domain.review;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
public class TranslatedReview {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "translated_review_id")
    private Long id;

    private String body; // 번역된 본문
    private String bodyLang; // 번역된 언어 코드

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "review_id") @NotNull
    private Review review;

    public TranslatedReview() {
    }

    @Builder
    public TranslatedReview(String body, String bodyLang, Review review) {
        this.body = body;
        this.bodyLang = bodyLang;
        this.review = review;
    }
}
