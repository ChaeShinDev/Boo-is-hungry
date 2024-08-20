package com.chaeshin.boo.service.notice.dto;

import com.chaeshin.boo.domain.Notice;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class NoticeListDto {

    private Long id;
    private String title;
    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    public NoticeListDto(Notice notice) {
        this.id = notice.getId();
        this.title = notice.getTitle();
        this.createdAt = notice.getCreatedAt();
    }
}
