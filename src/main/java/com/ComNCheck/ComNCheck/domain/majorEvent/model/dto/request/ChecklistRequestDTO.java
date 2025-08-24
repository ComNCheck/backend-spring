package com.ComNCheck.ComNCheck.domain.majorEvent.model.dto.request;

import lombok.Getter;

public class ChecklistRequestDTO {
    @Getter
    public static class CheckStatusUpdate {
        private Boolean isChecked;
    }
}
