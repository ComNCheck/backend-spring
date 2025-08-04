package com.ComNCheck.ComNCheck.domain.anotherEvent.model.dto.response;

import com.ComNCheck.ComNCheck.domain.anotherEvent.model.entity.AnotherEvent;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Builder
public class EventListResponseDTO {

    private Long id;
    private String eventName;
    private LocalDate date;
    private LocalTime time;
    private String location;

    public static EventListResponseDTO of(AnotherEvent anotherEvent) {
        return EventListResponseDTO.builder()
                .id(anotherEvent.getAnotherEventId())
                .eventName(anotherEvent.getEventName())
                .date(anotherEvent.getDate())
                .time(anotherEvent.getTime())
                .location(anotherEvent.getLocation())
                .build();
    }
}
