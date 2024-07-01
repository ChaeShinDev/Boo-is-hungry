package com.chaeshin.boo.repository;

import com.chaeshin.boo.domain.Notice;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class NoticeRepositoryTest {

    @Autowired NoticeRepository noticeRepository;

    @Test
    public void 공지사항_생성() throws Exception {
        /*given*/
        Notice notice = new Notice();

        /*when*/
        Notice savedNotice = noticeRepository.save(notice);

        /*then*/
        assertEquals(notice, savedNotice);
    }

    @Test
    public void 공지사항_리스트() throws Exception {
        /*given*/
        String title = "제목";
        String body = "내용";

        for (int i = 0; i < 25; i++) {
            Notice notice = noticeRepository.save(new Notice().builder().title(title).body(body).build());
        }
        /*when*/
        List<Notice> notices = noticeRepository.findAll();

        /*then*/
        assertEquals(25, notices.size());
    }

    @Test
    public void 공지사항_제목_내용_일치() throws Exception {
        /*given*/
        String title = "제목";
        String body = "내용";

        /*when*/
        Notice notice = noticeRepository.save(new Notice().builder().title(title).body(body).build());

        /*then*/
        assertEquals(notice.getTitle(), title);
        assertEquals(notice.getBody(), body);
    }
}
