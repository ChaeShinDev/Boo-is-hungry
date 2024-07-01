package com.chaeshin.boo.repository;

import com.chaeshin.boo.domain.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
}
