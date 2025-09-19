package com.ComNCheck.ComNCheck.domain.majorEvent.repository;

import com.ComNCheck.ComNCheck.domain.majorEvent.model.dto.response.EventListResponseDTO;
import com.ComNCheck.ComNCheck.domain.majorEvent.model.entity.MajorEvent;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.ComNCheck.ComNCheck.domain.majorEvent.model.entity.enums.HostType;
import com.ComNCheck.ComNCheck.domain.majorEvent.repository.querydsl.MajorEventRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MajorEventRepository extends JpaRepository<MajorEvent, Long>, MajorEventRepositoryCustom {
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

//    @Query("SELECT m FROM MajorEvent m WHERE YEAR(m.date) = :year")
//    List<MajorEvent> findAllByYear(@Param("year") int year);

    @Query("SELECT me FROM MajorEvent me WHERE " +
            "me.startDate <= :monthEnd AND (me.endDate IS NULL OR me.endDate >= :monthStart)")
    List<MajorEvent> findByDateRange(@Param("monthStart") LocalDate monthStart, @Param("monthEnd") LocalDate monthEnd);

    @Query("""
        SELECT e FROM MajorEvent e
        WHERE e.hostType = :hostType
          AND (e.startDate > :today OR (e.startDate = :today AND e.time >= :now))
        ORDER BY e.startDate DESC, e.time DESC
    """)
    List<MajorEvent> findNotPassedEventsByHostCategory(
            @Param("today") LocalDate today,
            @Param("now") LocalTime now,
            @Param("hostType") HostType hostType
    );
}
