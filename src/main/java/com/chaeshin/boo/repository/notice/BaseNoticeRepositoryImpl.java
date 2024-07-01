package com.chaeshin.boo.repository.notice;

import com.chaeshin.boo.domain.Notice;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class BaseNoticeRepositoryImpl implements BaseNoticeRepository {

    @PersistenceContext EntityManager em;

    @Override
    public void updateNotice(Long id, String newBody) {
        Notice notice = em.find(Notice.class, id);
        notice.updateNotice(newBody);
    }
}
