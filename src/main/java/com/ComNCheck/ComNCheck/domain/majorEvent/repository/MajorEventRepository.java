package com.ComNCheck.ComNCheck.domain.majorEvent.repository;

import com.ComNCheck.ComNCheck.domain.majorEvent.model.entity.MajorEvent;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MajorEventRepository extends JpaRepository<MajorEvent, Long> {
//    @Query("""
//        SELECT e
//        FROM MajorEvent e
//        WHERE (e.date > :today)
//           OR (e.date = :today AND e.time >= :currentTime)
//        ORDER BY e.date ASC, e.time ASC
//    """)
//    List<MajorEvent> findUpcomingEvents(
//            @Param("today") LocalDate today,
//            @Param("currentTime") LocalTime currentTime
//    );
}
