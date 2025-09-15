package com.ComNCheck.ComNCheck.domain.majorEvent.model.entity.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public enum EventType {

    FRESHMAN_ORIENTATION("새내기 배움터", FilterCategory.FRESHMAN_ORIENTATION),
    FIRST_SEMESTER_OPENING_MEETING("1학기 개강총회", FilterCategory.MEETING),
    FIRST_SEMESTER_CLOSING_MEETING("1학기 종강총회", FilterCategory.MEETING),
    SECOND_SEMESTER_OPENING_MEETING("2학기 개강총회", FilterCategory.MEETING),
    SECOND_SEMESTER_CLOSING_MEETING("2학기 종강총회", FilterCategory.MEETING),
    FACE_TO_FACE_MEETING("대면식", FilterCategory.FACE_TO_FACE_MEETING),
    FIRST_SEMESTER_MIDTERM_SNACK("1학기 중간고사 간식행사", FilterCategory.SNACK_EVENT),
    FIRST_SEMESTER_FINAL_SNACK("1학기 기말고사 간식행사", FilterCategory.SNACK_EVENT),
    SECOND_SEMESTER_MIDTERM_SNACK("2학기 중간고사 간식행사", FilterCategory.SNACK_EVENT),
    SECOND_SEMESTER_FINAL_SNACK("2학기 기말고사 간식행사", FilterCategory.SNACK_EVENT),
    MT("MT", FilterCategory.MT),
    KICK_OFF("해오름식", FilterCategory.KICK_OFF),
    COLLEGE_SPORTS_DAY("공대 체전", FilterCategory.SPORTS_DAY),
    UNIVERSITY_SPORTS_DAY("왕산 체전", FilterCategory.SPORTS_DAY),
    FESTIVAL("축제", FilterCategory.FESTIVAL),
    HOMECOMING_DAY("홈커밍 데이", FilterCategory.HOME_COMING_DAY),
    ETC("임의의 행사", FilterCategory.ETC);

    private final String displayName;
    private final FilterCategory filterCategory;

    EventType(String displayName, FilterCategory filterCategory) {
        this.displayName = displayName;
        this.filterCategory = filterCategory;
    }

    public static List<EventType> getByFilterCategory(FilterCategory category) {
        return Arrays.stream(EventType.values())
                .filter(eventType -> eventType.getFilterCategory() == category)
                .collect(Collectors.toList());
    }
}