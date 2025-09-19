package com.ComNCheck.ComNCheck.domain.majorEvent.model.dto.response;

import com.ComNCheck.ComNCheck.domain.majorEvent.model.entity.SuggestedEvent;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Getter
@Builder
public class SuggestedEventResponseDTO {
    private Long id;
    private String eventName;
    private String description;
    private String messageToCouncil; // 학생회에게 한마디 필드 추가
    private Long proposerId; // 제안한 사용자 정보
    private int likeCount;
    private boolean isLikedByCurrentUser;

    @Getter
    @Builder
    public static class LikeResponseDTO {
        private boolean isLiked; // 현재 사용자의 최종 '좋아요' 상태
        private int likeCount;   // 해당 행사의 최종 '좋아요' 개수
    }

    //단일 조회용(행사 수정)
    public static SuggestedEventResponseDTO from(SuggestedEvent event, boolean isLiked) {
        return SuggestedEventResponseDTO.builder()
                .id(event.getId())
                .eventName(event.getEventName())
                .description(event.getDescription())
                .messageToCouncil(event.getMessageToCouncil())
                .proposerId(event.getProposer().getMemberId())
                .likeCount(event.getLikeCount())
                .isLikedByCurrentUser(isLiked) // ★ 새로 생성된 행사이므로 '좋아요' 상태는 false
                .build();
    }

    //단일 조회용(행사 신청)
    public static SuggestedEventResponseDTO from(SuggestedEvent event) {
        return from(event, false); // 2번 메소드를 재활용
    }

    //행사 랭킹 조회용
    public static SuggestedEventResponseDTO from(SuggestedEvent event, Set<Long> likedEventIds) {
        boolean isLiked = likedEventIds != null && likedEventIds.contains(event.getId());
        return from(event, isLiked); // 2번 메소드를 재활용
    }
}
