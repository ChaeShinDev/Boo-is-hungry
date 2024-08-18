package com.chaeshin.boo.domain.restaurant;

import com.chaeshin.boo.domain.review.Review;
import com.chaeshin.boo.repository.restaurant.RestaurantRepository;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    // 편의 메서드
    /**
     * 새로 작성된 Review 의 평가를 반영하여 식당의 리뷰 평점 갱신.
     * @param newScore
     */
    public void updateScoreAvg(int newScore){
        this.reviewCnt ++; // Review가 새롭게 생성될 때만 호출되는 메서드임으로 Review 수를 하나 증가시켜준다.
        this.scoreAccum += newScore;
        this.scoreAvg = BigDecimal.valueOf(((double) this.scoreAccum / this.reviewCnt));
    }

}
