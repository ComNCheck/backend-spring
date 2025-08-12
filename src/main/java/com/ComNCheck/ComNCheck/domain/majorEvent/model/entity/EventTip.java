package com.ComNCheck.ComNCheck.domain.majorEvent.model.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class EventTip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 데이터베이스의 TEXT 타입과 매핑
    @Column(name="content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY) // 성능을 위해 지연 로딩 사용
    @JoinColumn(name = "event_id", nullable = false)
    private EventChecklist eventChecklist;
}
