package com.ComNCheck.ComNCheck.domain.majorQuestion.model.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnswerRequestDTO {
    private String content;
    private Long majorQuestionId;

}