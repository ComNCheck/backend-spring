package com.ComNCheck.ComNCheck.domain.employmentNotice.model.entity;

import com.ComNCheck.ComNCheck.domain.employmentNotice.model.dto.response.EmploymentNoticeResponseDTO;
import com.ComNCheck.ComNCheck.domain.majorNotice.model.dto.response.MajorNoticeResponseDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Table(name = "employment_notices")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class EmploymentNotice {
    @Id
    @Column(name = "employment_notice_id")
    private int employmentNoticeId;

    @Column
    private String title;

    @Column
    private LocalDate date;

    @Column
    private String link;

    public EmploymentNotice(EmploymentNoticeResponseDTO dto) {
        this.employmentNoticeId = dto.getEmploymentNoticeId();
        this.title = dto.getTitle();
        this.date = dto.getDate();
        this.link = dto.getLink();
    }

    public boolean updateFromDto(EmploymentNoticeResponseDTO dto) {
        boolean changed = false;

        if (!this.title.equals(dto.getTitle())) {
            this.title = dto.getTitle();
            changed = true;
        }
        if (!this.date.isEqual(dto.getDate())) {
            this.date = dto.getDate();
            changed = true;
        }
        if (!this.link.equals(dto.getLink())) {
            this.link = dto.getLink();
            changed = true;
        }
        return changed;
    }
}
