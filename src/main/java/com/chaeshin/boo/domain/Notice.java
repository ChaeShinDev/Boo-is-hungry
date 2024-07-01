package com.chaeshin.boo.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
public class Notice {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_id")
    private Long id;

    private String title;
    private String body;

    @CreatedDate // TZ 확인하기
    private LocalDateTime createdAt;

    // 편의 기능 메서드
    public void updateNotice(String newBody){this.body = newBody;}
}
