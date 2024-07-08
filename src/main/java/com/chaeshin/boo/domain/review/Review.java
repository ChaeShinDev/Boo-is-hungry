package com.chaeshin.boo.domain.review;

import com.chaeshin.boo.domain.Member;
import com.chaeshin.boo.domain.restaurant.Restaurant;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Review {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    @JoinColumn(name = "member_id")
    private Member member;

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

    /**
     * 필드를 인자로 갖는 Builder. Member, Restaurant은 필수.
     * @param member : <b>@NonNull</b>
     * @param restaurant : <b>@NonNull</b>
     * @param title
     * @param body
     * @param bodyLang
     * @param score
     */
    @Builder
    public Review(@NonNull Member member, @NonNull Restaurant restaurant, String title,
                  String body, String bodyLang, int score) {
        this.member = member;
        this.restaurant = restaurant;
        this.title = title;
        this.body = body;
        this.bodyLang = bodyLang;
        this.score = score;

        // 양방향 연관관계 맺어주기
        member.getReviews().add(this);
        restaurant.getReviews().add(this);
    }

}
