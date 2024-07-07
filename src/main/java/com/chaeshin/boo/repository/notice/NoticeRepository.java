package com.chaeshin.boo.repository.notice;

import com.chaeshin.boo.domain.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long>, BaseNoticeRepository {
}
