package com.ComNCheck.ComNCheck.domain.majorQuestion.model.dto.response;

import com.ComNCheck.ComNCheck.domain.majorQuestion.model.entity.Question;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class QuestionResponseDTO {
    private Long majorQuestionId;
    private String title;
    private String content;
    //private Long writerId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private AnswerResponseDTO answer;
    private boolean shared;

    public static QuestionResponseDTO of(Question question) {
        return QuestionResponseDTO.builder()
                .majorQuestionId(question.getId())
                .title(question.getTitle())
                .content(question.getContent())
                .shared(question.isShared())
                //.writerId(question.getWriter().getId())
                .createdAt(question.getCreatedAt())
                .updatedAt(question.getUpdatedAt())
                .answer(
                        question.getAnswer() != null ? AnswerResponseDTO.of(question.getAnswer()) : null
                )
                .build();
    }
}
