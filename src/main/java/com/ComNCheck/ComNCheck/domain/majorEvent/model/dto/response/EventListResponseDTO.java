package com.ComNCheck.ComNCheck.domain.majorEvent.model.dto.response;


import com.ComNCheck.ComNCheck.domain.majorEvent.model.entity.MajorEvent;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EventListResponseDTO {
    private Long id;
    private String eventName;
    private LocalDate date;
    private LocalTime time;
    private String googleFormLink;
    private String firstImageUrl;


    public static EventListResponseDTO of(MajorEvent majorEvent) {
        String firstImage = null;
        if (majorEvent.getCardNewsImageUrls() != null && !majorEvent.getCardNewsImageUrls().isEmpty()) {
            firstImage = majorEvent.getCardNewsImageUrls().get(0);
        }
        return EventListResponseDTO.builder()
                .id(majorEvent.getId())
                .eventName(majorEvent.getEventName())
//                .date(majorEvent.getDate())
                .time(majorEvent.getTime())
                .googleFormLink(majorEvent.getGoogleFormLink())
                .firstImageUrl(firstImage)
                .build();
    }

    public static AllEventsDTO toMajorEventNotPassed(MajorEvent majorEvent) {
        return AllEventsDTO.builder()
                .id(majorEvent.getId())
                .eventName(majorEvent.getEventName())
                .date(majorEvent.getStartDate())
                .time(majorEvent.getTime())
//                .googleFormLink(majorEvent.getGoogleFormLink())
                .location(majorEvent.getLocation())
                .build();
    }

    @Getter
    @Builder
    public static class EventByYearResponseDTO{
        Long majorEventId;
        String eventName;
        LocalDate startDate;
        LocalDate endDate;
        String location;
        String notice;
        private List<String> cardNewsImageUrls;
    }

    @Getter
    @Builder
    public static class AllEventsDTO{
        Long id;
        String eventName;
        private LocalDate date;
        private LocalTime time;
//        private String googleFormLink;
        private String location;
    }

}
