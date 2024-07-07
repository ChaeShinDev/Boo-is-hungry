package com.chaeshin.boo.domain.review;

import com.chaeshin.boo.domain.Member;
import com.chaeshin.boo.domain.restaurant.Restaurant;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
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

    // 양방향 연관관계 편의 메서드

    /**
     * Review - Member 양방향 관계 설정 편의 메서드
     * @param member
     */
    public void updateMember(Member member){
        this.member = member;
        member.getReviews().add(this);
    }

    /**
     * Review - Restaurant 양방향 관계 설정 편의 메서드
     * @param restaurant
     */
    public void updateRestaurant(Restaurant restaurant){
        this.restaurant = restaurant;
        restaurant.getReviews().add(this);
    }


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
