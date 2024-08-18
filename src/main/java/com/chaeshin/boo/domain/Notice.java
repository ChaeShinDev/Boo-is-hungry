package com.chaeshin.boo.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Notice {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_id")
    private Long id;

    private String title;
    private String body;

    @CreatedDate // TZ 확인하기
    private LocalDateTime createdAt;

    @Builder
    public Notice(@NonNull String title, @NonNull String body) {
        this.title = title;
        this.body = body;
    }

    // 편의 기능 메서드
    public void updateBody(String newBody){this.body = newBody;}

    public void updateTitle(String newTitle){this.title = newTitle;}
}
