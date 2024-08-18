package com.chaeshin.boo.service.notice;

import com.chaeshin.boo.domain.Notice;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class NoticeServiceTest {

    @Autowired
    NoticeService noticeService;


    @Test
    @DisplayName("공지사항 생성 및 조회")
    void createNotice(){
        // given
        Notice notice = new Notice();
        notice.updateTitle("제목");
        notice.updateBody("내용");

        System.out.println(notice.getCreatedAt());

        // when
        Long noticeId = noticeService.createNotice(notice);

        // then
        Assertions.assertNotNull(noticeService.getNoticeById(noticeId));
    }

    @Test
    @Transactional
    @DisplayName("공지사항 수정")
    void updateNotice(){
        // given
        Notice notice = new Notice();
        notice.updateTitle("제목");
        notice.updateBody("내용");

        Long noticeId = noticeService.createNotice(notice);

        // when
        Notice foundNotice = noticeService.getNoticeById(noticeId);
        foundNotice.updateBody("수정된 내용");
        foundNotice.updateTitle("수정된 제목");

        // then
        Assertions.assertEquals("수정된 제목", noticeService.getNoticeById(noticeId).getTitle());
        Assertions.assertEquals("수정된 내용", noticeService.getNoticeById(noticeId).getBody());
    }
}
