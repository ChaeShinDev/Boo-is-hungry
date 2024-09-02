package com.chaeshin.boo.service.notice;

import com.chaeshin.boo.repository.notice.NoticeRepository;
import com.chaeshin.boo.service.notice.dto.NoticeDetailDto;
import com.chaeshin.boo.service.notice.dto.NoticeListDto;
import com.chaeshin.boo.utils.ResponseDto;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeServiceImpl implements NoticeService {

    private final NoticeRepository noticeRepository;

    @Override
    public ResponseDto<List<NoticeListDto>> getNoticeList() {
        List<NoticeListDto> notices = noticeRepository.findAll()
                .stream()
                .map(NoticeListDto::new)
                .sorted(Comparator.comparing(NoticeListDto::getCreatedAt).reversed())
                .toList();

        return new ResponseDto<>("공지사항 목록 불러오기 성공", notices);
    }

    @Override
    public ResponseDto<NoticeDetailDto> getNoticeDetail(Long noticeId) {
        return new ResponseDto<>("공지사항 세부정보 불러오기 성공",
                new NoticeDetailDto(
                        noticeRepository.findById(noticeId).get()));
    }
}
