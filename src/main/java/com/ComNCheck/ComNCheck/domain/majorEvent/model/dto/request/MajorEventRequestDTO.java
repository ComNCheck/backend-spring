package com.ComNCheck.ComNCheck.domain.majorEvent.model.dto.request;

import com.ComNCheck.ComNCheck.domain.majorEvent.model.entity.enums.EventType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

public class MajorEventRequestDTO {

    @Getter
    @Setter
    public static abstract class EventCommonRequest {
        private String eventName;
        @Schema(description = "행사 카테고리(Enum 타입의 이름)", example = "MT")
        private EventType category;
        private String location;
        private String notice;
        private String googleFormLink;

        @Schema(type = "string", format = "date", description = "행사 시작일", example = "2025-08-12")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        private LocalDate startDate;

        @Schema(type = "string", format = "date", description = "행사 종료일", example = "2025-08-13")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        private LocalDate endDate;

        @Schema(type = "string", format = "time", description = "행사 시간 (HH:mm:ss 형식)", example = "14:30:00")
        private String time;
    }

    @Getter
    @Setter
    public static class Create extends EventCommonRequest {
        private List<MultipartFile> cardNewsImages;
    }

    @Getter
    @Setter
    public static class Update extends EventCommonRequest {
        private Long majorEventId;
        private List<String> existingImageUrls;
        private List<MultipartFile> newImages;
    }

    @Getter
    public static class Fix {
        private List<Long> tempEventIds;
    }

}
