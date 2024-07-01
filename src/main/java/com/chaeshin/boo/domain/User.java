package com.chaeshin.boo.domain;

import com.chaeshin.boo.domain.review.Review;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String googleId; // 구글 PK
    private String nickname;
    private String userLang; // 유저 선호 언어

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();

    public User() {}

    @Builder
    public User(String googleId, String nickname, String userLang) {
        this.googleId = googleId;
        this.nickname = nickname;
        this.userLang = userLang;
    }
}
