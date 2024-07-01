package com.chaeshin.boo.domain.review;

import com.chaeshin.boo.domain.User;
import com.chaeshin.boo.domain.restaurant.Restaurant;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Review {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL)
    private List<ReviewImage> reviewImages = new ArrayList<>();

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL)
    private List<TranslatedReview> translatedReviews = new ArrayList<>();

    private String title;
    private String body;
    private String bodyLang; // 본문의 언어코드
    private int score;

    @CreatedDate
    private LocalDateTime createdAt;


    // 편의 기능 메서드 - 수정 API 를 제공하는 필드에 대한 수정 메서드 정의

    /**
     * 리뷰 제목 수정
     * @param newTitle
     */
    public void updateTitle(String newTitle){this.title = newTitle;}


    /**
     * 리뷰 본문 수정
     * @param newBody
     */
    public void updateBody(String newBody){this.title = newBody;}


}
