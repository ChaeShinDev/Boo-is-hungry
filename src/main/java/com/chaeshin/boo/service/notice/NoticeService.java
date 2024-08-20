package com.chaeshin.boo.service.notice;

import com.chaeshin.boo.service.notice.dto.NoticeDetailDto;
import com.chaeshin.boo.service.notice.dto.NoticeListDto;
import com.chaeshin.boo.utils.ResponseDto;

import java.util.List;

public interface NoticeService {

    /*모든 공지사항 조회*/
    ResponseDto<List<NoticeListDto>> getNoticeList();

    /*공지사항 상세페이지*/
    ResponseDto<NoticeDetailDto> getNoticeDetail(Long noticeId);
}
