package com.ComNCheck.ComNCheck.domain.majorEvent.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.BatchSize;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class EventChecklist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="year", nullable = false)
    private int year;

    @Column(name="start_month", nullable = false)
    private int startMonth;

    @Column(name="end_month", nullable = false)
    private int endMonth;

    @Column(name="title", nullable = false)
    private String title;

    @Column(name = "event_order")
    private Integer eventOrder;

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "eventChecklist", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EventTip> tips = new ArrayList<>();

    @BatchSize(size=100)
    @OneToMany(mappedBy = "eventChecklist", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EventChecklistItem> items = new ArrayList<>();
}
