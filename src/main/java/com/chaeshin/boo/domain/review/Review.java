package com.chaeshin.boo.domain.review;

import com.chaeshin.boo.domain.User;
import com.chaeshin.boo.domain.restaurant.Restaurant;
import jakarta.persistence.*;
import lombok.Builder;
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

    public Review() {
    }

    @Builder
    public Review(User user, Restaurant restaurant, List<ReviewImage> reviewImages,
                  List<TranslatedReview> translatedReviews, String title,
                  String body, String bodyLang, int score) {
        this.user = user;
        this.restaurant = restaurant;
        this.title = title;
        this.body = body;
        this.bodyLang = bodyLang;
        this.score = score;
    }
}
