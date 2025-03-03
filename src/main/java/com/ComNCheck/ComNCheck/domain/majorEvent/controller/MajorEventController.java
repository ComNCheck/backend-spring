package com.ComNCheck.ComNCheck.domain.majorEvent.controller;

import com.ComNCheck.ComNCheck.domain.majorEvent.model.dto.request.EventCreateRequestDTO;
import com.ComNCheck.ComNCheck.domain.majorEvent.model.dto.request.EventUpdateRequestDTO;
import com.ComNCheck.ComNCheck.domain.majorEvent.model.dto.response.EventListResponseDTO;
import com.ComNCheck.ComNCheck.domain.majorEvent.model.dto.response.EventResponseDTO;
import com.ComNCheck.ComNCheck.domain.majorEvent.model.dto.response.PagedEventListResponseDTO;
import com.ComNCheck.ComNCheck.domain.majorEvent.service.MajorEventService;
import com.ComNCheck.ComNCheck.domain.security.oauth.CustomOAuth2Member;
import io.swagger.v3.oas.annotations.Operation;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/major-event")
@RequiredArgsConstructor
@RestController
public class MajorEventController {

    private final MajorEventService majorEventService;

    @PostMapping
    @Operation(summary = "과행사 게시글 작성", description = "과행사 게시글을 작성한다. 학생회, 과회장만 가능하다.")
    public ResponseEntity<EventResponseDTO> createMajorEvent(@ModelAttribute EventCreateRequestDTO requestDTO,
                                                             Authentication authentication) {
        // 문제 발생시 쌍따음표 일수도 있음
        CustomOAuth2Member principal = (CustomOAuth2Member) authentication.getPrincipal();
        Long memberId = principal.getMemberDTO().getMemberId();
        EventResponseDTO responseDTO = majorEventService.createMajorEvent(requestDTO, memberId);
        return ResponseEntity.ok(responseDTO);
    }


    @GetMapping("/{majorEventId}")
    @Operation(summary = "특정 과행사 게시글 조회", description = "특정 과행사 게시글을 조회한다.")
    public ResponseEntity<EventResponseDTO> getMajorEvent(@PathVariable Long majorEventId) {
        EventResponseDTO responseDTO = majorEventService.getMajorEvent(majorEventId);
        return ResponseEntity.ok(responseDTO);
    }


    @GetMapping
    @Operation(summary = "과행사 게시글 목록 조회", description = "과행사 게시글 목록을 조회한다. 이미 지난 행사는 보여주지 않는다.")
    public ResponseEntity<List<EventListResponseDTO>> getAllMajorEventsNotPassed() {
        List<EventListResponseDTO> allMajorEventsNotPassed = majorEventService.getAllMajorEventsNotPassed();
        return ResponseEntity.ok(allMajorEventsNotPassed);
    }


    @PutMapping("/{majorEventId}")
    @Operation(summary = "과행사 게시글 수정", description = "작성된 과행사 게시글을 수정한다. 작성자가 누구든 과회장과, 학생회는 수정할 수 있다.")
    public ResponseEntity<EventResponseDTO> updateMajorEvent(
            @PathVariable Long majorEventId,
            @ModelAttribute EventUpdateRequestDTO requestDTO,
            Authentication authentication
    ) {
        CustomOAuth2Member principal = (CustomOAuth2Member) authentication.getPrincipal();
        Long memberId = principal.getMemberDTO().getMemberId();
        EventResponseDTO updateDTO = majorEventService.updateMajorEvent(majorEventId, requestDTO, memberId);
        return ResponseEntity.ok(updateDTO);
    }


    @DeleteMapping("/{majorEventId}")
    @Operation(summary = "과행사 게시글 삭제 ", description = "작성된 과행사 게시글을 삭제한다. 작성자가 누구든 과회장과, 학생회는 삭제할 수 있다.")
    public ResponseEntity<Void> deleteMajorEvent(@PathVariable Long majorEventId,
                                                 Authentication authentication) {
        CustomOAuth2Member principal = (CustomOAuth2Member) authentication.getPrincipal();
        Long memberId = principal.getMemberDTO().getMemberId();
        majorEventService.deleteMajorEvent(majorEventId, memberId);
        return ResponseEntity.noContent().build();
    }

}
