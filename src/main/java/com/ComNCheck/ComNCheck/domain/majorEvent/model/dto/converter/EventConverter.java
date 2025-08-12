package com.ComNCheck.ComNCheck.domain.majorEvent.model.dto.converter;

import com.ComNCheck.ComNCheck.domain.majorEvent.model.dto.response.ChecklistResponseDTO;
import com.ComNCheck.ComNCheck.domain.majorEvent.model.dto.response.EventListResponseDTO;
import com.ComNCheck.ComNCheck.domain.majorEvent.model.dto.response.EventResponseDTO;
import com.ComNCheck.ComNCheck.domain.majorEvent.model.entity.EventChecklist;
import com.ComNCheck.ComNCheck.domain.majorEvent.model.entity.MajorEvent;

import java.util.List;
import java.util.stream.Collectors;

public class EventConverter {
    /**
     * 최종 응답 객체(MonthlyChecklistDTO)를 생성합니다.
     * @param startMonth 검색 시작월
     * @param endMonth 검색 종료월
     * @param checklists 변환된 EventChecklistDTO 리스트
     * @return 최종 응답 DTO
     */
    public static ChecklistResponseDTO.MonthlyChecklistDTO toMonthlyChecklistResponse(
            int startMonth, int endMonth, List<ChecklistResponseDTO.EventChecklistDTO> checklists) {

        return ChecklistResponseDTO.MonthlyChecklistDTO.builder()
                .startMonth(startMonth)
                .endMonth(endMonth)
                .checklists(checklists)
                .build();
    }

    /**
     * EventChecklist 엔티티 리스트를 DTO 리스트로 변환하는 메인 메소드입니다.
     * @param checklists 조회된 엔티티 리스트
     * @return 변환된 DTO 리스트
     */
    public static List<ChecklistResponseDTO.EventChecklistDTO> toEventChecklists(List<EventChecklist> checklists) {
        return checklists.stream()
                .map(EventConverter::toEventChecklist) // 아래의 단일 변환 메소드를 재사용
                .collect(Collectors.toList());
    }

    /**
     * 단일 EventChecklist 엔티티를 EventChecklistDTO로 변환하는 핵심 로직입니다.
     * 연관된 자식 엔티티(tips, items)들도 함께 DTO로 변환합니다.
     * @param entity 변환할 EventChecklist 엔티티
     * @return 변환된 EventChecklistDTO
     */
    private static ChecklistResponseDTO.EventChecklistDTO toEventChecklist(EventChecklist entity) {
        List<ChecklistResponseDTO.TipDTO> tipDTOs = entity.getTips().stream()
                .map(tip ->
                        ChecklistResponseDTO.TipDTO.builder()
                            .id(tip.getId())
                            .content(tip.getContent())
                            .build())
                .toList();

        List<ChecklistResponseDTO.ItemDTO> itemDTOs = entity.getItems().stream()
                .map(item ->
                        ChecklistResponseDTO.ItemDTO.builder()
                                .id(item.getId())
                                .content(item.getContent())
                                .isChecked(item.isChecked())
                                .build())
                .toList();

        // 최종적으로 EventChecklistDTO를 생성하여 반환
        return ChecklistResponseDTO.EventChecklistDTO.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .tips(tipDTOs)
                .checklists(itemDTOs)
                .build();
    }



    public static List<EventListResponseDTO.EventByYearResponseDTO> toYearlyEventResponse(List<MajorEvent> events) {
        return events.stream()
                .map(event -> EventListResponseDTO.EventByYearResponseDTO.builder()
                        .majorEventId(event.getId())
                        .eventName(event.getEventName())
                        .location(event.getLocation())
                        .notice(event.getNotice())
//                        .date(event.getDate())
                        .cardNewsImageUrls(event.getCardNewsImageUrls())
                        .build()
                )
                .toList();
    }


    public static EventResponseDTO.Count toCountResponse(Long count){
        return EventResponseDTO.Count.builder().count(count).build();
    }

    public static List<EventResponseDTO> toSubmitResponse(List<MajorEvent> majorEvents){
        return majorEvents.stream()
                .map(EventResponseDTO::from)
                .toList();
    }
}
