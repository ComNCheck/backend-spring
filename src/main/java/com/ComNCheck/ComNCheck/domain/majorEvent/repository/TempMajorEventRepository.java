package com.ComNCheck.ComNCheck.domain.majorEvent.repository;

import com.ComNCheck.ComNCheck.domain.majorEvent.model.entity.TempMajorEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TempMajorEventRepository extends JpaRepository<TempMajorEvent, Long> {
    @Query("SELECT tme FROM TempMajorEvent tme WHERE " +
            "tme.startDate <= :monthEnd AND (tme.endDate IS NULL OR tme.endDate >= :monthStart)")
    List<TempMajorEvent> findByDateRange(@Param("monthStart") LocalDate monthStart, @Param("monthEnd") LocalDate monthEnd);
}
