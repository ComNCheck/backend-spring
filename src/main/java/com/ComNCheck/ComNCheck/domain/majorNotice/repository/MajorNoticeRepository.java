package com.ComNCheck.ComNCheck.domain.majorNotice.repository;

import com.ComNCheck.ComNCheck.domain.majorNotice.model.entity.MajorNotice;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MajorNoticeRepository extends JpaRepository<MajorNotice, Integer> {
    Optional<MajorNotice> findByNoticeId(int noticeId);

}
