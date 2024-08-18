package com.chaeshin.boo.controller.dto.notice;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;


@Data
public class NoticeDetailDto extends NoticeSimpleDto {

    @JsonProperty("body")
    private String body;

    public NoticeDetailDto(Long id, String title, LocalDateTime createdAt, String body) {
        super(id, title, createdAt);
        this.body = body;
    }

}
