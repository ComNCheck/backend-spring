package com.ComNCheck.ComNCheck.domain.majorEvent.model.dto.response;

import com.ComNCheck.ComNCheck.domain.majorEvent.model.entity.TempMajorEvent;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Builder
public class TempEventResponseDTO {
    private Long tempEventId;
    private String eventName;
    private String category;
    private String hostType;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime time;
    private String location;
    private String notice;
    private String googleFormLink;
    private List<String> cardNewsImageUrls;

    public static TempEventResponseDTO from(TempMajorEvent event) {
        String category = event.getCategory() != null ? event.getCategory().name() : null;
        String hostType = event.getHostType() != null ? event.getHostType().name() : null;

        return TempEventResponseDTO.builder()
                .tempEventId(event.getId())
                .eventName(event.getEventName())
                .category(category) // Enum -> String
                .hostType(hostType)
                .startDate(event.getStartDate())
                .endDate(event.getEndDate())
                .time(event.getTime())
                .location(event.getLocation())
                .notice(event.getNotice())
                .googleFormLink(event.getGoogleFormLink())
                .cardNewsImageUrls(event.getCardNewsImageUrls())
                .build();
    }

    @Builder
    public static class Fix{
        private Long Id;
        private String eventName;
    }
}
