package com.ComNCheck.ComNCheck.domain.majorEvent.controller;

import com.ComNCheck.ComNCheck.domain.majorEvent.model.dto.request.EventCreateRequestDTO;
import com.ComNCheck.ComNCheck.domain.majorEvent.model.dto.request.EventUpdateRequestDTO;
import com.ComNCheck.ComNCheck.domain.majorEvent.model.dto.response.EventListResponseDTO;
import com.ComNCheck.ComNCheck.domain.majorEvent.model.dto.response.EventResponseDTO;
import com.ComNCheck.ComNCheck.domain.majorEvent.model.dto.response.PagedEventListResponseDTO;
import com.ComNCheck.ComNCheck.domain.majorEvent.service.MajorEventService;
import com.ComNCheck.ComNCheck.domain.security.oauth.CustomOAuth2Member;
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
    public ResponseEntity<EventResponseDTO> createMajorEvent(@ModelAttribute EventCreateRequestDTO requestDTO,
                                                             Authentication authentication) {
        // 문제 발생시 쌍따음표 일수도 있음
        CustomOAuth2Member principal = (CustomOAuth2Member) authentication.getPrincipal();
        Long memberId = principal.getMemberDTO().getMemberId();
        EventResponseDTO responseDTO = majorEventService.createMajorEvent(requestDTO, memberId);
        return ResponseEntity.ok(responseDTO);
    }


    @GetMapping("/{majorEventId}")
    public ResponseEntity<EventResponseDTO> getMajorEvent(@PathVariable Long majorEventId) {
        EventResponseDTO responseDTO = majorEventService.getMajorEvent(majorEventId);
        return ResponseEntity.ok(responseDTO);
    }


    @GetMapping
    public ResponseEntity<List<EventListResponseDTO>> getAllMajorEventsNotPassed() {
        List<EventListResponseDTO> allMajorEventsNotPassed = majorEventService.getAllMajorEventsNotPassed();
        return ResponseEntity.ok(allMajorEventsNotPassed);
    }


    @PutMapping("/{majorEventId}")
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
    public ResponseEntity<Void> deleteMajorEvent(@PathVariable Long majorEventId,
                                                 Authentication authentication) {
        CustomOAuth2Member principal = (CustomOAuth2Member) authentication.getPrincipal();
        Long memberId = principal.getMemberDTO().getMemberId();
        majorEventService.deleteMajorEvent(majorEventId, memberId);
        return ResponseEntity.noContent().build();
    }

}
