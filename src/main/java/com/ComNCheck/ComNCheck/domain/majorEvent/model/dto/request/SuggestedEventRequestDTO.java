package com.ComNCheck.ComNCheck.domain.majorEvent.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class SuggestedEventRequestDTO {

    @Getter
    @NoArgsConstructor
    public static class Create {
        @NotBlank(message = "행사명은 필수 입력 항목입니다.")
        private String eventName;

        @NotBlank(message = "행사 소개는 필수 입력 항목입니다.")
        private String description;

        private String messageToCouncil;
    }

    /**
     * 제안된 행사를 수정할 때 사용하는 DTO
     */
    @Getter
    @NoArgsConstructor
    public static class SuggestedEventUpdate {
        @NotBlank(message = "행사명은 필수 입력 항목입니다.")
        private String eventName;

        @NotBlank(message = "행사 소개는 필수 입력 항목입니다.")
        private String description;

        private String messageToCouncil;
    }
}
