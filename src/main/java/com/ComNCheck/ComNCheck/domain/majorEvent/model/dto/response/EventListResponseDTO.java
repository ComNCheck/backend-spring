package com.ComNCheck.ComNCheck.domain.majorEvent.model.dto.response;


import com.ComNCheck.ComNCheck.domain.majorEvent.model.entity.MajorEvent;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EventListResponseDTO {
    private String eventName;
    private LocalDate date;
    private LocalTime time;
    private String googleFormLink;

    public static EventListResponseDTO of(MajorEvent majorEvent) {
        return EventListResponseDTO.builder()
                .eventName(majorEvent.getEventName())
                .date(majorEvent.getDate())
                .time(majorEvent.getTime())
                .build();
    }
}
