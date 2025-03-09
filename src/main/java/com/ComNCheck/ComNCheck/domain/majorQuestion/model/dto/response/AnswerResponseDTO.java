package com.ComNCheck.ComNCheck.domain.majorQuestion.model.dto.response;

import com.ComNCheck.ComNCheck.domain.majorQuestion.model.entity.Answer;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AnswerResponseDTO {
    private Long answerId;
    private String content;
    private Long majorQuestionId;
    private Long writerId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static AnswerResponseDTO of(Answer answer) {
        return AnswerResponseDTO.builder()
                .answerId(answer.getId())
                .content(answer.getContent())
                .majorQuestionId(answer.getQuestion().getId())
                .writerId(answer.getWriter().getMemberId())
                .createdAt(answer.getCreatedAt())
                .updatedAt(answer.getUpdatedAt())
                .build();
    }
}
