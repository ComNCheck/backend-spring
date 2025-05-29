package com.ComNCheck.ComNCheck.domain.anotherEvent.model.entity;

import com.ComNCheck.ComNCheck.domain.member.model.entity.Member;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class AnotherEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long anotherEventId;

    @Column(name = "event_name", nullable = false)
    private String eventName;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "time", nullable = false)
    private LocalTime time;

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "notice", nullable = false, columnDefinition = "TEXT")
    private String notice;

    @Column(name = "google_form_link")
    private String googleFormLink;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "anotehr_event_card_news_images", joinColumns = @JoinColumn(name = "event_id"))
    @Column(name = "image_url")
    private List<String> cardNewsImageUrls = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "writer_id")
    private Member writer;

    @Builder
    public AnotherEvent(Member writer, String eventName, LocalDate date, LocalTime time,
                        String location, String notice, String googleFormLink,
                        List<String> cardNewsImageUrls) {
        this.writer = writer;
        this.eventName = eventName;
        this.date = date;
        this.time = time;
        this.location = location;
        this.notice = notice;
        this.googleFormLink = googleFormLink;
        if (cardNewsImageUrls != null) {
            this.cardNewsImageUrls = cardNewsImageUrls;
        }
    }

    public void updateEvent(String eventName, LocalDate date, LocalTime time,
                            String location, String notice, String googleFormLink) {
        this.eventName = eventName;
        this.date = date;
        this.time = time;
        this.location = location;
        this.notice = notice;
        this.googleFormLink = googleFormLink;
    }

    public void updateCardNewsImages(List<String> newImageUrls) {
        this.cardNewsImageUrls.clear();
        this.cardNewsImageUrls.addAll(newImageUrls);
    }
}
