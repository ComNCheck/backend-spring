package com.ComNCheck.ComNCheck.domain.majorNotice.repository;


import com.ComNCheck.ComNCheck.domain.majorNotice.model.entity.MajorNotice;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MajorNoticeRepository extends JpaRepository<MajorNotice, Integer> {
    Optional<MajorNotice> findByNoticeId(int noticeId);
    @Query("SELECT e FROM MajorNotice e ORDER BY e.noticeId DESC")
    List<MajorNotice> findAllOrderedById();


}
