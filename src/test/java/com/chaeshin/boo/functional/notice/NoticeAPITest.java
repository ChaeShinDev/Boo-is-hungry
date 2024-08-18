package com.chaeshin.boo.functional.notice;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.chaeshin.boo.domain.Notice;
import com.chaeshin.boo.service.notice.NoticeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class NoticeAPITest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    NoticeService noticeService;


    @Test
    @DisplayName("GET : notice/")
    void getNoticeList() throws Exception {
        // given
        Notice notice1 = new Notice();
        Notice notice2 = new Notice();

        notice1.updateTitle("제목1");
        notice1.updateBody("내용1");

        notice2.updateTitle("제목2");
        notice2.updateBody("내용2");

        // when
        noticeService.createNotice(notice1);
        noticeService.createNotice(notice2);

        // then
        mockMvc.perform(get("/notice/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.size()").value(2));
    }

    @Test
    @DisplayName("GET : notice/{noticeId}/")
    void getDetailNotice() throws Exception {
        // given
        Notice notice = new Notice();
        notice.updateTitle("제목");
        notice.updateBody("내용");

        Long noticeId = noticeService.createNotice(notice);

        // when
        mockMvc.perform(get("/notice/" + noticeId + "/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title").value("제목"))
                .andExpect(jsonPath("$.data.body").value("내용"));
    }
}
