package com.chaeshin.boo.controller.dto.notice;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

/**
 * 공지사항 목록 전체 조회시 사용되는 DTO.
 */
@Data
public class NoticeSimpleDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    public NoticeSimpleDto(Long id, String title, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.createdAt = createdAt;
    }
}
