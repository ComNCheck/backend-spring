package com.ComNCheck.ComNCheck.domain.employmentNotice.repository;

import com.ComNCheck.ComNCheck.domain.employmentNotice.model.entity.EmploymentNotice;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EmploymentNoticeRepository extends JpaRepository<EmploymentNotice, Integer> {
    Optional<EmploymentNotice> findByEmploymentNoticeId(int employmentNoticeId);

    @Query("SELECT e FROM EmploymentNotice e ORDER BY e.employmentNoticeId DESC")
    List<EmploymentNotice> findAllOrderedById();
}
