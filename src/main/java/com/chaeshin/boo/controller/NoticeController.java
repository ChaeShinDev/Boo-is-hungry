package com.chaeshin.boo.controller;

import com.chaeshin.boo.service.notice.NoticeService;
import com.chaeshin.boo.service.notice.dto.NoticeDetailDto;
import com.chaeshin.boo.service.notice.dto.NoticeListDto;
import com.chaeshin.boo.utils.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notice")
public class NoticeController {

    private final NoticeService noticeService;

    @GetMapping("/")
    public ResponseEntity<ResponseDto<List<NoticeListDto>>> getNotices() {
        try {
            return new ResponseEntity<>(
                    noticeService.getNoticeList(), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{noticeId}/")
    public ResponseEntity<ResponseDto<NoticeDetailDto>> getNotice(
            @PathVariable Long noticeId) {
        try {
            return new ResponseEntity<>(
                    noticeService.getNoticeDetail(noticeId), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
