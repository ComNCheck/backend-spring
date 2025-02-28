package com.ComNCheck.ComNCheck.domain.majorEvent.model.dto.response;


import com.ComNCheck.ComNCheck.domain.majorEvent.model.entity.MajorEvent;
import java.time.LocalDate;
import java.time.LocalTime;
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
                .id(majorEvent.getMajorEventId())
                .eventName(majorEvent.getEventName())
                .date(majorEvent.getDate())
                .time(majorEvent.getTime())
                .googleFormLink(majorEvent.getGoogleFormLink())
                .firstImageUrl(firstImage)
                .build();
    }
}
