package com.chaeshin.boo.domain.restaurant;

import com.chaeshin.boo.domain.review.Review;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Restaurant {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "restaurant_id")
    private Long id;

    private String restaurantName;
    private String restaurantImageUrl;
    private String latitude; // 위도
    private String longitude; // 경도
    private String openingHours;
    private String address;
    private String phone;
    private int reviewCnt; // 리뷰 개수
    private int scoreAccum; // 리뷰 누적 점수

    @Column(precision = 3, scale = 1)
    private BigDecimal scoreAvg; // 리뷰 평균 점수

    @Enumerated
    private Category category;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    private List<Menu> menus = new ArrayList<>();
}
