package com.chaeshin.boo.domain.review;

import jakarta.persistence.*;
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
    @JoinColumn(name = "review_id")
    private Review review;
}
