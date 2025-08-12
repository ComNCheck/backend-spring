package com.ComNCheck.ComNCheck.domain.majorEvent.model.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class EventChecklistItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="content", nullable = false)
    private String content;

    @Column(name="is_checked", nullable = false)
    private boolean isChecked;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private EventChecklist eventChecklist;

    public void check() {
        this.isChecked = true;
    }

    public void uncheck() {
        this.isChecked = false;
    }
}