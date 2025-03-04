package com.ComNCheck.ComNCheck.domain.majorNotice.model.entity;

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
@Table(name = "major_notices")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class MajorNotice {

    @Id
    @Column(name = "major_notice_id")
    private int noticeId;

    @Column
    private String title;

    @Column
    private LocalDate date;

    @Column
    private String link;

    public MajorNotice(MajorNoticeResponseDTO dto) {
        this.noticeId = dto.getNoticeId();
        this.title = dto.getTitle();
        this.date = dto.getDate();
        this.link = dto.getLink();
    }

    public boolean equalsDTO(MajorNoticeResponseDTO dto) {
        return this.noticeId== dto.getNoticeId() &&
                this.title.equals(dto.getTitle()) &&
                this.date.isEqual(dto.getDate()) &&
                this.link.equals(dto.getLink());
    }
    public boolean updateFromDto(MajorNoticeResponseDTO dto) {
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
