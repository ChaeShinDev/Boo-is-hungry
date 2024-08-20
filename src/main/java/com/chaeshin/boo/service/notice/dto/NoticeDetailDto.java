package com.chaeshin.boo.service.notice.dto;

import com.chaeshin.boo.domain.Notice;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class NoticeDetailDto {

    private Long id;
    private String title;
    private String body;
    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    public NoticeDetailDto(Notice notice) {
        this.id = notice.getId();
        this.title = notice.getTitle();
        this.body = notice.getBody();
        this.createdAt = notice.getCreatedAt();
    }
}
