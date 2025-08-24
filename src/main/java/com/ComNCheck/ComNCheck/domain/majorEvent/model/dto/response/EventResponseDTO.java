package com.ComNCheck.ComNCheck.domain.majorEvent.model.dto.response;

import com.ComNCheck.ComNCheck.domain.majorEvent.model.entity.MajorEvent;
import com.ComNCheck.ComNCheck.domain.majorEvent.model.entity.enums.EventType;
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
    private EventType category;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime time;
    private String location;
    private String notice;
    private String googleFormLink;
    private List<String> cardNewsImageUrls;

    public static EventResponseDTO of(MajorEvent majorEvent) {
        return EventResponseDTO.builder()
                .id(majorEvent.getId())
                .eventName(majorEvent.getEventName())
//                .date(majorEvent.getDate())
                .startDate(majorEvent.getStartDate())
                .endDate(majorEvent.getEndDate())
                .time(majorEvent.getTime())
                .location(majorEvent.getLocation())
                .notice(majorEvent.getNotice())
                .googleFormLink(majorEvent.getGoogleFormLink())
                .cardNewsImageUrls(majorEvent.getCardNewsImageUrls())
                .build();
    }

    public static EventResponseDTO from(MajorEvent event) {
        return EventResponseDTO.builder()
                .id(event.getId())
                .eventName(event.getEventName())
                .category(event.getCategory()) // Enum -> String
                .startDate(event.getStartDate())
                .endDate(event.getEndDate())
                .time(event.getTime())
                .location(event.getLocation())
                .notice(event.getNotice())
                .googleFormLink(event.getGoogleFormLink())
                .cardNewsImageUrls(event.getCardNewsImageUrls())
                .build();
    }

    @Getter
    @Builder
    public static class Count{
        private Long count;
    }
}
