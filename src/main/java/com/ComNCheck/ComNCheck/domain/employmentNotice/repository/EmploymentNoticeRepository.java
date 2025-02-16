package com.ComNCheck.ComNCheck.domain.employmentNotice.repository;

import com.ComNCheck.ComNCheck.domain.employmentNotice.model.entity.EmploymentNotice;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmploymentNoticeRepository extends JpaRepository<EmploymentNotice, Integer> {
    Optional<EmploymentNotice> findByEmploymentNoticeId(int employmentNoticeId);
}
