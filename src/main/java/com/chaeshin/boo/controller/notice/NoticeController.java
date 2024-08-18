package com.chaeshin.boo.controller.notice;

import com.chaeshin.boo.controller.dto.BaseResponseDto;
import com.chaeshin.boo.controller.dto.DataResponseDto;
import com.chaeshin.boo.controller.dto.notice.NoticeDetailDto;
import com.chaeshin.boo.controller.dto.notice.NoticeSimpleDto;
import com.chaeshin.boo.domain.Notice;
import com.chaeshin.boo.service.notice.NoticeService;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class NoticeController {

    private final NoticeService noticeService;

    /**
     * 공지사항 전체 조회
     *
     * @return 공지사항 리스트. 공지사항이 없을 시 빈 리스트 반환.
     */
    @GetMapping("notice/")
    public ResponseEntity<BaseResponseDto> getAllNotice(){
        return ResponseEntity.ok().body(new DataResponseDto<>("공지사항 목록 불러오기성공", noticeService.getAll().stream()
                .map(notice -> new NoticeSimpleDto(notice.getId(), notice.getTitle(), notice.getCreatedAt()))
                .collect(Collectors.toList())));
    }

    /**
     * 공지사항 세부정보 조회
     * @param noticeId
     * @return
     */
    @GetMapping("notice/{notice_id}/")
    public ResponseEntity<BaseResponseDto> getNoticeDetail(@PathVariable("notice_id") Long noticeId){

        try {
            Notice found = noticeService.getNoticeById(noticeId);
            NoticeDetailDto dto = new NoticeDetailDto(found.getId(), found.getTitle(), found.getCreatedAt(), found.getBody());

            return ResponseEntity.ok().body(new DataResponseDto<>("공지사항 세부정보 불러오기 성공", dto));
        }
        catch (NoSuchElementException e){
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(new BaseResponseDto("해당 공지사항이 존재하지 않습니다."));
        }
    }
}
