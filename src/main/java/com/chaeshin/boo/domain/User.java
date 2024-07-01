package com.chaeshin.boo.domain;

import com.chaeshin.boo.domain.review.Review;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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


    // 편의 기능 메서드 : 추후 서비스 상 변경을 제공하는 경우를 위해 정의한다
    public void updateUserNickname(String nickname){
        this.nickname = nickname;
    }
}
