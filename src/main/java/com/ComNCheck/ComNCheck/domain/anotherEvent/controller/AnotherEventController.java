package com.ComNCheck.ComNCheck.domain.anotherEvent.controller;

import com.ComNCheck.ComNCheck.domain.anotherEvent.model.dto.request.EventCreateRequestDTO;
import com.ComNCheck.ComNCheck.domain.anotherEvent.model.dto.request.EventUpdateRequestDTO;
import com.ComNCheck.ComNCheck.domain.anotherEvent.model.dto.response.EventListResponseDTO;
import com.ComNCheck.ComNCheck.domain.anotherEvent.model.dto.response.EventResponseDTO;
import com.ComNCheck.ComNCheck.domain.anotherEvent.service.AnotherEventService;
import com.ComNCheck.ComNCheck.domain.security.oauth.CustomOAuth2Member;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/another-event")
@RequiredArgsConstructor
@RestController
public class AnotherEventController {

    private final AnotherEventService anotherEventService;

    @Operation(
            summary = "AnotherEvent 게시글 작성",
            description = "AnotherEvent 게시글을 작성한다. 관리자, 과회장, 학생회만 가능하다."
    )
    @PostMapping
    public ResponseEntity<EventResponseDTO> createAnotherEvent(
            @ModelAttribute EventCreateRequestDTO requestDTO,
            Authentication authentication
    ) {
        CustomOAuth2Member principal = (CustomOAuth2Member) authentication.getPrincipal();
        Long memberId = principal.getMemberDTO().getMemberId();
        EventResponseDTO responseDTO = anotherEventService.createAnotherEvent(requestDTO, memberId);
        return ResponseEntity.ok(responseDTO);
    }

    @Operation(
            summary = "특정 AnotherEvent 게시글 조회",
            description = "특정 AnotherEvent 게시글을 조회한다."
    )
    @GetMapping("/{anotherEventId}")
    public ResponseEntity<EventResponseDTO> getAnotherEvent(
            @PathVariable Long anotherEventId
    ) {
        EventResponseDTO responseDTO = anotherEventService.getAnotherEvent(anotherEventId);
        return ResponseEntity.ok(responseDTO);
    }

    @Operation(
            summary = "AnotherEvent 게시글 목록 조회",
            description = "AnotherEvent 게시글 목록을 조회한다. 이미 지난 행사는 제외된다."
    )
    @GetMapping
    public ResponseEntity<List<EventListResponseDTO>> getAllAnotherEventsNotPassed() {
        List<EventListResponseDTO> list = anotherEventService.getAllAnotherEventsNotPassed();
        return ResponseEntity.ok(list);
    }

    @Operation(
            summary = "AnotherEvent 게시글 수정",
            description = "작성된 AnotherEvent 게시글을 수정한다. 관리자, 과회장, 학생회만 가능하다."
    )
    @PutMapping("/{anotherEventId}")
    public ResponseEntity<EventResponseDTO> updateAnotherEvent(
            @PathVariable Long anotherEventId,
            @ModelAttribute EventUpdateRequestDTO requestDTO,
            Authentication authentication
    ) {
        CustomOAuth2Member principal = (CustomOAuth2Member) authentication.getPrincipal();
        Long memberId = principal.getMemberDTO().getMemberId();
        EventResponseDTO updateDTO = anotherEventService.updateAnotherEvent(anotherEventId, requestDTO, memberId);
        return ResponseEntity.ok(updateDTO);
    }

    @Operation(
            summary = "AnotherEvent 게시글 삭제",
            description = "작성된 AnotherEvent 게시글을 삭제한다. 관리자, 과회장, 학생회만 가능하다."
    )
    @DeleteMapping("/{anotherEventId}")
    public ResponseEntity<Void> deleteAnotherEvent(
            @PathVariable Long anotherEventId,
            Authentication authentication
    ) {
        CustomOAuth2Member principal = (CustomOAuth2Member) authentication.getPrincipal();
        Long memberId = principal.getMemberDTO().getMemberId();
        anotherEventService.deleteAnotherEvent(anotherEventId, memberId);
        return ResponseEntity.noContent().build();
    }
}
