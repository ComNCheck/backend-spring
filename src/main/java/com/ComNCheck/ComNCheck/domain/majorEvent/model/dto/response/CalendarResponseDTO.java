package com.ComNCheck.ComNCheck.domain.majorEvent.model.dto.response;

import com.ComNCheck.ComNCheck.domain.majorEvent.model.entity.MajorEvent;
import com.ComNCheck.ComNCheck.domain.majorEvent.model.entity.TempMajorEvent;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class CalendarResponseDTO {
    private Long id;
    private String eventName;
    private LocalDate startDate;
    private LocalDate endDate;
    private EventStatus eventStatus;

    public enum EventStatus {
        TEMPORARY, // 임시 저장
        FIXED      // 확정
    }

    // TempMajorEvent를 DTO로 변환하는 정적 팩토리 메소드
    public static CalendarResponseDTO from(TempMajorEvent tempEvent) {
        return CalendarResponseDTO.builder()
                .id(tempEvent.getId())
                .eventName(tempEvent.getEventName())
                .startDate(tempEvent.getStartDate())
                .endDate(tempEvent.getEndDate())
                .eventStatus(EventStatus.TEMPORARY)
                .build();
    }

    // MajorEvent를 DTO로 변환하는 정적 팩토리 메소드
    public static CalendarResponseDTO from(MajorEvent majorEvent) {
        return CalendarResponseDTO.builder()
                .id(majorEvent.getId())
                .eventName(majorEvent.getEventName())
                .startDate(majorEvent.getStartDate())
                .endDate(majorEvent.getEndDate())
                .eventStatus(EventStatus.FIXED)
                .build();
    }
}
