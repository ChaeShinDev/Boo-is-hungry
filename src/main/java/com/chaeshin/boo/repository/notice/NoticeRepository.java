package com.chaeshin.boo.repository.notice;

import com.chaeshin.boo.domain.Notice;
import org.springframework.data.repository.CrudRepository;

public interface NoticeRepository extends CrudRepository<Notice, Long>, BaseNoticeRepository {
}
