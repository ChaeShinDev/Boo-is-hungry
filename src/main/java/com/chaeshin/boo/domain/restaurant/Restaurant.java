package com.chaeshin.boo.domain.restaurant;

import com.chaeshin.boo.domain.review.Review;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(indexes = @Index(name = "restaurant_index", columnList = "name"))
public class Restaurant {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "restaurant_id")
    private Long id;

    private String name;
    private String imageUrl;
    private String latitude; // 위도
    private String longitude; // 경도
    private String businessHours;
    private String address;
    private String phone;
    private int reviewCnt; // 리뷰 개수
    private int scoreAccum; // 리뷰 누적 점수

    @Column(precision = 2, scale = 1)
    private BigDecimal scoreAvg; // 리뷰 평균 점수

    @Enumerated(EnumType.STRING)
    private Category category;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    private List<Menu> menus = new ArrayList<>();

    @Builder
    public Restaurant(String name, String imageUrl, String latitude,
                      String longitude, String businessHours,
                      String address, String phone, int reviewCnt,
                      int scoreAccum, BigDecimal scoreAvg, Category category) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.latitude = latitude;
        this.longitude = longitude;
        this.businessHours = businessHours;
        this.address = address;
        this.phone = phone;
        this.reviewCnt = reviewCnt;
        this.scoreAccum = scoreAccum;
        this.scoreAvg = scoreAvg;
        this.category = category;
    }

    public void addReview(Review review) {
        reviews.add(review);
        this.reviewCnt += 1;
        this.scoreAccum += review.getScore();
        this.scoreAvg = BigDecimal.valueOf((double)this.scoreAccum / this.reviewCnt);
    }

    public void updateReview(int diff) {
        this.scoreAccum += diff;
        this.scoreAvg = BigDecimal.valueOf((double)this.scoreAccum / this.reviewCnt);
    }

    public void deleteReview(Review review) {
        this.reviewCnt -= 1;
        this.scoreAccum -= review.getScore();
        if (reviewCnt == 0) {
            this.scoreAccum = 0;
        } else {
            this.scoreAvg = BigDecimal.valueOf((double)this.scoreAccum / this.reviewCnt);
        }
        reviews.remove(review);
    }
}
