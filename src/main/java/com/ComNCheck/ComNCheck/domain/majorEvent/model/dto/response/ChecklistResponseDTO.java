package com.ComNCheck.ComNCheck.domain.majorEvent.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ChecklistResponseDTO {
    /**
     * 개별 행사 체크리스트 정보를 담는 DTO
     */
    @Getter
    @Builder
    public static class EventChecklistDTO {
        private Long id;
        private String title;
        private List<TipDTO> tips;
        private List<ItemDTO> checklists;
    }

    /**
     * 월별 행사 체크리스트 정보를 담는 DTO
     */
    @Getter
    @Builder
    public static class MonthlyChecklistDTO {
        private int startMonth;
        private int endMonth;
        private List<EventChecklistDTO> checklists;
    }

    /**
     * 준비 TIP 정보를 담는 DTO
     */
    @Getter
    @Builder
    public static class TipDTO {
        private Long id;
        private String content;
    }

    /**
     * 체크리스트 항목 정보를 담는 DTO - 체크 여부 포함
     */
    @Getter
    @Builder
    public static class ItemDTO {
        private Long id;
        private String content;

        @Getter(onMethod_ = @JsonProperty("isChecked"))
        private boolean isChecked;
    }
}
