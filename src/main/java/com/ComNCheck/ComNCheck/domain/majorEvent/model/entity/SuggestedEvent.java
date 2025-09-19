package com.ComNCheck.ComNCheck.domain.majorEvent.model.entity;

import com.ComNCheck.ComNCheck.domain.member.model.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class SuggestedEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String eventName; // 행사명 (예: "가을학기 E-Sports 대회")

    @Column(nullable = false, length = 1000)
    private String description; // 행사 소개 (예: "롤, 발로란트 종목으로...")

    @Column(length = 1000)
    private String messageToCouncil; // 학생회에게 한마디

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proposer_id")
    private Member proposer; // 제안한 사용자

    private int likeCount = 0; // 좋아요 수 (랭킹을 위해)

    // 좋아요 수를 안전하게 변경하는 편의 메소드
    public void incrementLikeCount() {
        this.likeCount++;
    }
    public void decrementLikeCount() {
        if (this.likeCount > 0) {
            this.likeCount--;
        }
    }
    public void update(String eventName, String description, String messageToCouncil) {
        this.eventName = eventName;
        this.description = description;
        this.messageToCouncil = messageToCouncil;
    }
}