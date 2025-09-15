package com.ComNCheck.ComNCheck.domain.majorEvent.model.entity;

import com.ComNCheck.ComNCheck.domain.majorEvent.model.entity.enums.EventType;
import com.ComNCheck.ComNCheck.domain.majorEvent.model.entity.enums.HostType;
import com.ComNCheck.ComNCheck.domain.member.model.entity.Member;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class MajorEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_name")
    private String eventName;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_category")
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

//    @Column(name = "date", nullable = false)
//    private LocalDate date;

    @Column(name = "time", nullable = false)
    private LocalTime time;

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "notice", nullable = false, columnDefinition = "TEXT")
    private String notice;

    @Column(name = "google_form_link")
    private String googleFormLink;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "event_card_news_images", joinColumns = @JoinColumn(name = "event_id"))
    @Column(name = "image_url")
    @Builder.Default
    private List<String> cardNewsImageUrls = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "writer_id")
    private Member writer;

//    @Builder
//    public MajorEvent(Member writer, String eventName, EventType category, LocalDate startDate, LocalDate endDate, LocalDate date, LocalTime time,
//                 String location, String notice, String googleFormLink,
//                 List<String> cardNewsImageUrls) {
//        this.writer = writer;
//        this.eventName = eventName;
//        this.category = category;
//        this.startDate = startDate;
//        this.endDate = endDate;
//        this.date = date;
//        this.time = time;
//        this.location = location;
//        this.notice = notice;
//        this.googleFormLink = googleFormLink;
//        if (cardNewsImageUrls != null) {
//            this.cardNewsImageUrls = cardNewsImageUrls;
//        }
//    }

    public void update(
            String eventName,
            EventType category,
            HostType hostType,
            LocalDate startDate,
            LocalDate endDate,
//            LocalDate date,
            LocalTime time,
            String location,
            String notice,
            String googleFormLink,
            List<String> finalImageUrls

    ) {
        this.eventName = eventName;
        this.category = category;
        this.hostType = hostType;
        this.startDate = startDate;
        this.endDate = endDate;
//        this.date = date;
        this.time = time;
        this.location = location;
        this.notice = notice;
        this.googleFormLink = googleFormLink;
        this.cardNewsImageUrls = finalImageUrls;

        if (startDate != null) {
            this.startDate = startDate;
            this.year = this.startDate.getYear();
        }
    }

    public void updateCardNewsImages(List<String> newImageUrls) {
        this.cardNewsImageUrls.clear();
        this.cardNewsImageUrls.addAll(newImageUrls);
    }
}
