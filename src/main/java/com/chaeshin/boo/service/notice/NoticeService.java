package com.chaeshin.boo.service.notice;

import com.chaeshin.boo.domain.Notice;
import com.chaeshin.boo.repository.notice.NoticeRepository;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeService {

    private final NoticeRepository noticeRepository;


    /**
     * @return 공지 전체 조회. 등록된 공지가 없을 시 빈 리스트 반환.
     */
    public List<Notice> getAll(){
        return noticeRepository.findAll();
    }

    /**
     * 공지 생성.
     * @param notice
     * @return 생성된 공지 PK id.
     */
    public Long createNotice(Notice notice){
        return noticeRepository.save(notice).getId();
    }

    /**
     * 공지 단 건 조회.
     * @param noticeId
     * @return Notice
     * @throws NoSuchElementException
     */
    public Notice getNoticeById(Long noticeId) throws NoSuchElementException {

        Optional<Notice> found = noticeRepository.findById(noticeId);

        if(found.isPresent()){
            return found.get();
        }
        else {
            throw new NoSuchElementException("해당 공지가 존재하지 않습니다.");
        }
    }

}
