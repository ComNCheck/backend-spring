package com.ComNCheck.ComNCheck.domain.majorNotice.model.dto.response;


import com.ComNCheck.ComNCheck.domain.majorNotice.model.entity.MajorNotice;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MajorNoticeResponseDTO {
    @JsonProperty("notice_id")
    private int noticeId;
    private String title;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd")
    private LocalDate date;
    private String link;

    public static MajorNoticeResponseDTO of(MajorNotice majorNotice) {
        return MajorNoticeResponseDTO.builder()
                .noticeId(majorNotice.getNoticeId())
                .title(majorNotice.getTitle())
                .date(majorNotice.getDate())
                .link(majorNotice.getLink())
                .build();
    }
}
