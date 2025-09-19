package com.ComNCheck.ComNCheck.domain.majorEvent.controller;

import com.ComNCheck.ComNCheck.domain.majorEvent.model.dto.request.*;
import com.ComNCheck.ComNCheck.domain.majorEvent.model.dto.response.*;
import com.ComNCheck.ComNCheck.domain.majorEvent.model.entity.enums.FilterCategory;
import com.ComNCheck.ComNCheck.domain.majorEvent.model.entity.enums.HostType;
import com.ComNCheck.ComNCheck.domain.majorEvent.service.CalenderService;
import com.ComNCheck.ComNCheck.domain.majorEvent.service.ChecklistService;
import com.ComNCheck.ComNCheck.domain.majorEvent.service.MajorEventService;
import com.ComNCheck.ComNCheck.domain.security.auth.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/major-event")
@RequiredArgsConstructor
@RestController
public class MajorEventController {

    private final MajorEventService majorEventService;
    private final ChecklistService checklistService;
    private final CalenderService calenderService;

//    @PostMapping
//    @Operation(summary = "과행사 게시글 작성", description = "과행사 게시글을 작성한다. 학생회, 과회장만 가능하다.")
//    public ResponseEntity<EventResponseDTO> createMajorEvent(@ModelAttribute EventCreateRequestDTO requestDTO,
//                                                             Authentication authentication) {
//        // 문제 발생시 쌍따음표 일수도 있음
//        CustomOAuth2Member principal = (CustomOAuth2Member) authentication.getPrincipal();
//        Long memberId = principal.getMemberDTO().getMemberId();
//        EventResponseDTO responseDTO = majorEventService.createMajorEvent(requestDTO, memberId);
//        return ResponseEntity.ok(responseDTO);
//    }


    @GetMapping("/{majorEventId}")
    @Operation(summary = "특정 과행사 게시글 조회", description = "특정 과행사 게시글을 조회한다.")
    public ResponseEntity<EventResponseDTO.EventDTO> getMajorEvent(
            @PathVariable Long majorEventId) {
        return ResponseEntity.ok(majorEventService.getMajorEvent(majorEventId));
    }


    @GetMapping
    @Operation(summary = "과행사 게시글 목록 조회", description = "과행사 게시글 목록을 조회한다. 이미 지난 행사는 보여주지 않는다.")
    public ResponseEntity<List<EventListResponseDTO.AllEventsDTO>> getAllMajorEventsNotPassed(@RequestParam HostType hostCategory) {
        List<EventListResponseDTO.AllEventsDTO> allMajorEventsNotPassed = majorEventService.getAllMajorEventsNotPassed(hostCategory);
        return ResponseEntity.ok(allMajorEventsNotPassed);
    }


//    @PutMapping("/{majorEventId}")
//    @Operation(summary = "과행사 게시글 수정", description = "작성된 과행사 게시글을 수정한다. 작성자가 누구든 과회장과, 학생회는 수정할 수 있다.")
//    public ResponseEntity<EventResponseDTO> updateMajorEvent(
//            @PathVariable Long majorEventId,
//            @ModelAttribute EventUpdateRequestDTO requestDTO,
//            Authentication authentication
//    ) {
//        CustomOAuth2Member principal = (CustomOAuth2Member) authentication.getPrincipal();
//        Long memberId = principal.getMemberDTO().getMemberId();
//        EventResponseDTO updateDTO = majorEventService.updateMajorEvent(majorEventId, requestDTO, memberId);
//        return ResponseEntity.ok(updateDTO);
//    }
//
//    @DeleteMapping("/{majorEventId}")
//    @Operation(summary = "과행사 게시글 삭제 ", description = "작성된 과행사 게시글을 삭제한다. 작성자가 누구든 과회장과, 학생회는 삭제할 수 있다.")
//    public ResponseEntity<Void> deleteMajorEvent(@PathVariable Long majorEventId,
//                                                 Authentication authentication) {
//        CustomOAuth2Member principal = (CustomOAuth2Member) authentication.getPrincipal();
//        Long memberId = principal.getMemberDTO().getMemberId();
//        majorEventService.deleteMajorEvent(majorEventId, memberId);
//        return ResponseEntity.noContent().build();
//    }

//    @DeleteMapping("/{majorEventId}")
//    @Operation(summary = "과행사 게시글 삭제 ", description = "작성된 과행사 게시글을 삭제한다. 작성자가 누구든 과회장과, 학생회는 삭제할 수 있다.")
//    public ResponseEntity<Void> deleteMajorEvent(@PathVariable Long majorEventId,
//                                                 Authentication authentication) {
//        CustomOAuth2Member principal = (CustomOAuth2Member) authentication.getPrincipal();
//        Long memberId = principal.getMemberDTO().getMemberId();
//        majorEventService.deleteMajorEvent(majorEventId, memberId);
//        return ResponseEntity.noContent().build();
//    }

    //월별 체크리스트 조회
    @GetMapping("/checklists/monthly")
    @Operation(summary = "월별 체크리스트 조회", description = "특정 기간(월 단위)에 해당하는 행사의 체크리스트와 준비 TIP을 조회합니다.")
    public ResponseEntity<ChecklistResponseDTO.MonthlyChecklistDTO> getMonthlyChecklist(
            @RequestParam int startMonth,
            @RequestParam int endMonth,
//            @AuthenticationPrincipal CustomOAuth2Member principal
            @AuthenticationPrincipal CustomUserDetails principal
    ) {
//        Long memberId = principal.getMemberDTO().getMemberId();
//        CustomOAuth2Member principal = (CustomOAuth2Member) authentication.getPrincipal();
        Long memberId = principal.getMemberDTO().getMemberId();

        return ResponseEntity.ok(checklistService.getMonthlyChecklist(startMonth, endMonth, memberId));
    }

    //체크리스트 내 체크 상태 업데이트
    @PutMapping("/checklists/{itemId}")
    @Operation(summary = "체크리스트 항목 상태 변경", description = "특정 체크리스트 항목의 완료/미완료(체크/해제) 상태를 업데이트합니다.")
    public ResponseEntity<ChecklistResponseDTO.ItemDTO> updateChecklistItemStatus(
            @PathVariable Long itemId,
            @RequestBody ChecklistRequestDTO.CheckStatusUpdate requestDTO,
//            @AuthenticationPrincipal CustomOAuth2Member principal
            @AuthenticationPrincipal CustomUserDetails principal
    ) {

        Long memberId = principal.getMemberDTO().getMemberId();

        ChecklistResponseDTO.ItemDTO updatedItem = checklistService.updateChecklistItemStatus(itemId, requestDTO.getIsChecked(), memberId);

        return ResponseEntity.ok(updatedItem);
    }

    //카테고리 혹은 연도별 진행했던 행사 조회
    @GetMapping("/eventlists")
    @Operation(summary = "행사 목록 동적 조회", description = "연도 또는 카테고리를 기준으로 행사 목록을 조회합니다. 두 조건은 함께 사용할 수 없습니다.")
    public ResponseEntity<List<EventListResponseDTO.EventByYearResponseDTO>> getEventsByYear(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) FilterCategory category,
//            @AuthenticationPrincipal CustomOAuth2Member principal
            @AuthenticationPrincipal CustomUserDetails principal
    ) {
        Long memberId = principal.getMemberDTO().getMemberId();

        return ResponseEntity.ok(majorEventService.searchEvents(year, category, memberId));
    }

    //임시 행사 저장
    @PostMapping(value = "/temp", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "임시 행사 저장", description = "새로운 행사를 임시 저장합니다. `multipart/form-data` 형식으로 요청해야 합니다.")
    public ResponseEntity<TempEventResponseDTO> saveTempEvent(
            @ModelAttribute TempEventRequestDTO.TempEventCreate requestDto,
//            @AuthenticationPrincipal CustomOAuth2Member principal
            @AuthenticationPrincipal CustomUserDetails principal
    ) {
        Long memberId = principal.getMemberDTO().getMemberId();
        return ResponseEntity.ok(calenderService.saveTempEvent(requestDto, memberId));
    }

    //임시 행사 수정
    @PatchMapping(value = "/temp/{tempEventId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "임시 행사 수정", description = "임시 저장된 행사의 내용을 수정합니다. `multipart/form-data` 형식으로 요청해야 합니다.")
    public ResponseEntity<TempEventResponseDTO> updateTempEvent(
            @PathVariable Long tempEventId,
            @ModelAttribute TempEventRequestDTO.TempEventUpdate requestDto,
//            @AuthenticationPrincipal CustomOAuth2Member principal
            @AuthenticationPrincipal CustomUserDetails principal
    ) {
        Long memberId = principal.getMemberDTO().getMemberId();
        return ResponseEntity.ok(calenderService.updateTempEvent(tempEventId, requestDto, memberId));
    }

    //임시 행사 삭제
    @DeleteMapping("/temp/{tempEventId}")
    @Operation(summary = "임시 행사 삭제", description = "임시 저장된 행사를 삭제합니다.")
    public ResponseEntity<Void> deleteTempEvent(
            @PathVariable Long tempEventId,
//            @AuthenticationPrincipal CustomOAuth2Member principal
            @AuthenticationPrincipal CustomUserDetails principal
    ) {
        Long memberId = principal.getMemberDTO().getMemberId();

        calenderService.deleteTempEvent(tempEventId, memberId);
        return ResponseEntity.ok().build();
    }

    //행사 픽스
    @PostMapping("/fix")
    @Operation(summary = "임시 행사 최종 제출", description = "선택된 임시 행사들을 최종 확정된 행사로 저장하고, 임시 저장 목록에서 삭제합니다.")
    public ResponseEntity<List<EventResponseDTO>> submitAllTempEvents(
            @RequestBody TempEventRequestDTO.Fix requestDto,
//            @AuthenticationPrincipal CustomOAuth2Member principal
            @AuthenticationPrincipal CustomUserDetails principal
    ) {
        Long memberId = principal.getMemberDTO().getMemberId();

        return ResponseEntity.ok(calenderService.submitAllTempEvents(requestDto, memberId));
    }

    //달력 조회
    @GetMapping("/calendar")
    @Operation(summary = "월별 달력 조회", description = "특정 연도와 월에 해당하는 모든 행사(확정/임시) 목록을 조회합니다. 권한에 따라 임시 행사 포함 여부가 결정됩니다.")
    public ResponseEntity<List<CalendarResponseDTO>> getCalendarEvents(
            @RequestParam int year,
            @RequestParam int month,
//            @AuthenticationPrincipal CustomOAuth2Member principal
            @AuthenticationPrincipal CustomUserDetails principal
    ) {
        Long memberId = principal.getMemberDTO().getMemberId();

        return ResponseEntity.ok(calenderService.getCalendarEvents(year, month, memberId));
    }

    //남은 행사 갯수
    @GetMapping("/count")
    @Operation(summary = "남은 행사 갯수 조회", description = "사용자가 작성한 임시 저장 행사 갯수를 조회합니다.")
    public ResponseEntity<EventResponseDTO.Count> getTempEventCount(
//            @AuthenticationPrincipal CustomOAuth2Member principal
            @AuthenticationPrincipal CustomUserDetails principal
    ) {
        Long memberId = principal.getMemberDTO().getMemberId();

        return ResponseEntity.ok(majorEventService.countAllEvents(memberId));
    }

    // 확정된 행사 수정
    @PutMapping(value = "/{majorEventId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "행사 수정", description = "확정된 과행사를 수정한다. 과회장과 학생회만 수정할 수 있다.")
        public ResponseEntity<EventResponseDTO> updateMajorEvent(
            @PathVariable Long majorEventId,
            @ModelAttribute MajorEventRequestDTO.MajorEventUpdate requestDTO,
//            @AuthenticationPrincipal CustomOAuth2Member principal
            @AuthenticationPrincipal CustomUserDetails principal
    ) {
        Long memberId = principal.getMemberDTO().getMemberId();
        return ResponseEntity.ok(majorEventService.updateMajorEvent(majorEventId, requestDTO, memberId));
    }

    // 확정된 행사 삭제
    @DeleteMapping("/{majorEventId}")
    @Operation(summary = "행사 삭제 ", description = "확정된 과행사를 삭제한다. 과회장과 학생회만 삭제할 수 있다.")
    public ResponseEntity<Void> deleteMajorEvent(
            @PathVariable Long majorEventId,
//            @AuthenticationPrincipal CustomOAuth2Member principal
            @AuthenticationPrincipal CustomUserDetails principal
    ) {
        Long memberId = principal.getMemberDTO().getMemberId();
        majorEventService.deleteMajorEvent(majorEventId, memberId);
        return ResponseEntity.ok().build();
    }
}
