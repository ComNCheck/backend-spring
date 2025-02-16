package com.ComNCheck.ComNCheck.domain.majorQuestion.model.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionRequestDTO {
    private String title;
    private String content;
    private boolean shared;
}
