package com.ComNCheck.ComNCheck.domain.anotherEvent.model.dto.response;

import com.ComNCheck.ComNCheck.domain.anotherEvent.model.entity.AnotherEvent;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Builder
public class EventResponseDTO {

    private Long id;
    private String eventName;
    private LocalDate date;
    private LocalTime time;
    private String location;
    private String notice;
    private String googleFormLink;
    private List<String> cardNewsImageUrls;

    public static EventResponseDTO of(AnotherEvent anotherEvent) {
        return EventResponseDTO.builder()
                .id(anotherEvent.getAnotherEventId())
                .eventName(anotherEvent.getEventName())
                .date(anotherEvent.getDate())
                .time(anotherEvent.getTime())
                .location(anotherEvent.getLocation())
                .notice(anotherEvent.getNotice())
                .googleFormLink(anotherEvent.getGoogleFormLink())
                .cardNewsImageUrls(anotherEvent.getCardNewsImageUrls())
                .build();
    }
}
