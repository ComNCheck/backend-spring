package com.ComNCheck.ComNCheck.domain.majorEvent.model.entity;

import com.ComNCheck.ComNCheck.domain.majorEvent.model.entity.enums.EventType;
import com.ComNCheck.ComNCheck.domain.majorEvent.model.entity.enums.HostType;
import com.ComNCheck.ComNCheck.domain.member.model.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TempMajorEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "writer_id", nullable = false)
    private Member writer;

    @Column(name = "event_name", nullable = false)
    private String eventName;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_category", nullable = false)
    private EventType category;

    @Enumerated(EnumType.STRING)
    @Column(name = "host_category", nullable = false)
    private HostType hostType;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "year", nullable = false)
    private int year;

    @Column(name = "time")
    private LocalTime time;

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "notice", nullable = false, columnDefinition = "TEXT")
    private String notice;

    @Column(name = "google_form_link")
    private String googleFormLink;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "temp_event_card_news_images", joinColumns = @JoinColumn(name = "event_id"))
    @Column(name = "image_url")
    @Builder.Default
    private List<String> cardNewsImageUrls = new ArrayList<>();

    public void update(
            String eventName,
            EventType category,
            HostType hostType,
            LocalDate startDate,
            LocalDate endDate,
            LocalTime time,
            String location,
            String notice,
            String googleFormLink,
            List<String> cardNewsImageUrls
    ) {
        this.eventName = eventName;
        this.category = category;
        this.hostType = hostType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.time = time;
        this.location = location;
        this.notice = notice;
        this.googleFormLink = googleFormLink;
        this.cardNewsImageUrls = cardNewsImageUrls;

        // startDate가 변경되면, year도 함께 변경하여 데이터 정합성을 유지합니다.
        if (startDate != null) {
            this.startDate = startDate;
            this.year = this.startDate.getYear();
        }
    }

    public MajorEvent toMajorEvent() {
        return MajorEvent.builder()
                .writer(this.writer)
                .eventName(this.eventName)
                .startDate(this.startDate)
                .endDate(this.endDate)
                .year(this.year)
                .time(this.time)
                .location(this.location)
                .notice(this.notice)
                .googleFormLink(this.googleFormLink)
                .category(this.category)
                .hostType(this.hostType)
                .cardNewsImageUrls(new ArrayList<>(this.cardNewsImageUrls))
                .build();
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public void setCategory(EventType category) {
        this.category = category;
    }
}