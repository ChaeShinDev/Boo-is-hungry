package com.chaeshin.boo.repository.notice;

import com.chaeshin.boo.domain.Notice;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class NoticeRepositoryTest {

    @Autowired
    NoticeRepository noticeRepository;

    @Test
    void 공지_생성(){
        // given
        Notice notice = new Notice();
        ReflectionTestUtils.setField(notice, "id", 1L);

        // when
        Notice created = noticeRepository.save(notice);

        // then
        Assertions.assertNotNull(created);
    }

    @Test
    void 공지_조회(){
        //given
        Notice notice = new Notice();
        ReflectionTestUtils.setField(notice, "id", 1L);

        //when
        Notice created = noticeRepository.save(notice);
        Optional<Notice> found = noticeRepository.findById(created.getId());

        // then
        Assertions.assertTrue(found.isPresent());
        Assertions.assertEquals(found.get().getId(), created.getId());
    }

    @Test
    void 공지_수정(){

        // given
        Notice notice = new Notice();
        ReflectionTestUtils.setField(notice, "id", 1L);
        noticeRepository.save(notice);

        // when
        noticeRepository.updateNotice(notice.getId(), "updated!");
        Optional<Notice> found = noticeRepository.findById(notice.getId());

        // then
        Assertions.assertTrue(found.isPresent());
        Assertions.assertEquals(found.get().getBody(), "updated!");

    }

    @Test
    void 공지_삭제(){
        //given
        Notice notice = new Notice();
        ReflectionTestUtils.setField(notice, "id", 1L);

        // when
        Notice created = noticeRepository.save(notice);
        noticeRepository.delete(notice);
        Optional<Notice> found = noticeRepository.findById(created.getId());

        // then
        Assertions.assertFalse(found.isPresent());

    }
}
