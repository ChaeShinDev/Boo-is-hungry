package com.chaeshin.boo.domain;

import com.chaeshin.boo.domain.review.Review;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import java.util.ArrayList;
import java.util.List;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@Getter
@NoArgsConstructor
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String googleId; // 구글 PK
    private String nickname;
    private String memberLang; // 유저 선호 언어

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();

    @Builder
    public Member(@NonNull String googleId, @NonNull String nickname, @NonNull String memberLang) {
        this.googleId = googleId;
        this.nickname = nickname;
        this.memberLang = memberLang;
    }

    // 편의 기능 메서드 : 추후 서비스 상 변경을 제공하는 경우를 위해 정의한다
    public void updateMemberNickname(String nickname){
        this.nickname = nickname;
    }
}
