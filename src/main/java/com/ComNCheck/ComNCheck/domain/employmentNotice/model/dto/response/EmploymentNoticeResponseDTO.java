package com.ComNCheck.ComNCheck.domain.employmentNotice.model.dto.response;

import com.ComNCheck.ComNCheck.domain.employmentNotice.model.entity.EmploymentNotice;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EmploymentNoticeResponseDTO {
    @JsonProperty("notice_id")
    private int employmentNoticeId;
    private String title;
    @JsonFormat(shape = Shape.STRING, pattern = "yyyy.MM.dd")
    private LocalDate date;
    private String link;

    public static EmploymentNoticeResponseDTO of(EmploymentNotice employmentNotice) {
        return EmploymentNoticeResponseDTO.builder()
                .employmentNoticeId(employmentNotice.getEmploymentNoticeId())
                .title(employmentNotice.getTitle())
                .date(employmentNotice.getDate())
                .link(employmentNotice.getLink())
                .build();
    }
}
