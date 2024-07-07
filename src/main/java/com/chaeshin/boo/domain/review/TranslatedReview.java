package com.chaeshin.boo.domain.review;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
    @NotNull
    @JoinColumn(name = "review_id")
    private Review review;

    // 생성자 메서드
    public static TranslatedReview createTranslatedReview(Review review){
        TranslatedReview tr = new TranslatedReview();
        tr.review = review;
        review.getTranslatedReviews().add(tr);

        return tr;
    }

    // 편의 기능 메서드
    public void updateBody(String newBody){this.body = newBody;}
}
