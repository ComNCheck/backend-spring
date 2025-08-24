package com.ComNCheck.ComNCheck.domain.majorEvent.repository;

import com.ComNCheck.ComNCheck.domain.majorEvent.model.entity.EventChecklist;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EventChecklistRepository extends JpaRepository<EventChecklist, Long> {

    @Query("SELECT DISTINCT ec FROM EventChecklist ec " +
            "LEFT JOIN FETCH ec.items " +
            "WHERE ec.startMonth <= :endMonth AND ec.endMonth >= :startMonth"
            )
    List<EventChecklist> findByMonthRange(@Param("startMonth") int startMonth, @Param("endMonth") int endMonth);
}