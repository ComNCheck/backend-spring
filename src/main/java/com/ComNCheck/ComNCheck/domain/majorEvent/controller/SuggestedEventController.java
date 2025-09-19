package com.ComNCheck.ComNCheck.domain.majorEvent.controller;

import com.ComNCheck.ComNCheck.domain.majorEvent.model.dto.request.SuggestedEventRequestDTO;
import com.ComNCheck.ComNCheck.domain.majorEvent.model.dto.response.SuggestedEventResponseDTO;
import com.ComNCheck.ComNCheck.domain.majorEvent.service.SuggestedEventService;
import com.ComNCheck.ComNCheck.domain.security.auth.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/suggested-event")
@RequiredArgsConstructor
@RestController
public class SuggestedEventController {
    private final SuggestedEventService suggestedEventService;

    //행사 제안
    @PostMapping
    @Operation(summary = "새로운 행사 제안 등록", description = "사용자가 원하는 새로운 행사를 제안하여 등록합니다.")
    public ResponseEntity<SuggestedEventResponseDTO> createSuggestedEvent(
            @RequestBody SuggestedEventRequestDTO.Create requestDto,
            @AuthenticationPrincipal CustomUserDetails principal) {

        Long memberId = principal.getMemberDTO().getMemberId();
        return ResponseEntity.ok(suggestedEventService.createSuggestedEvent(requestDto, memberId));
    }

    //행사 수정
    @PutMapping("/{eventId}")
    @Operation(summary = "제안된 행사 수정", description = "자신이 제안한 행사의 내용을 수정합니다.")
    public ResponseEntity<SuggestedEventResponseDTO> updateSuggestedEvent(
            @PathVariable Long eventId,
            @RequestBody SuggestedEventRequestDTO.SuggestedEventUpdate requestDto,
            @AuthenticationPrincipal CustomUserDetails principal) {

        Long memberId = principal.getMemberDTO().getMemberId();

        return ResponseEntity.ok(suggestedEventService.updateSuggestedEvent(eventId, requestDto, memberId));
    }

    //행사 좋아요
    @PostMapping("/{eventId}/like")
    @Operation(summary = "제안 행사 '좋아요' 상태 변경", description = "특정 제안 행사에 '좋아요'를 누르거나 취소합니다.")
    public ResponseEntity<SuggestedEventResponseDTO.LikeResponseDTO> toggleEventLike(
            @PathVariable Long eventId,
            @AuthenticationPrincipal CustomUserDetails principal) {

        Long memberId = principal.getMemberDTO().getMemberId();

        return ResponseEntity.ok(suggestedEventService.toggleLike(eventId, memberId));
    }

    //전체 제안 행사 조회
    @GetMapping
    @Operation(summary = "전체 제안 행사 목록 조회 (페이징)", description = "모든 제안 행사 목록을 페이징하여 조회합니다. 정렬 옵션 사용 가능 (예: ?sort=likeCount,desc)")
    public ResponseEntity<Page<SuggestedEventResponseDTO>> getAllSuggestedEvents(
            @AuthenticationPrincipal CustomUserDetails principal,
            @ParameterObject Pageable pageable) {

        Long memberId = principal.getMemberDTO().getMemberId();
        Page<SuggestedEventResponseDTO> eventPage = suggestedEventService.getAllSuggestedEvents(memberId, pageable);
        return ResponseEntity.ok(eventPage);
    }

    //내가 제안한 행사 조회
    @GetMapping("/my")
    @Operation(summary = "내가 제안한 행사 목록 조회 (페이징)", description = "현재 로그인한 사용자가 제안한 모든 행사 목록을 페이징하여 조회합니다.")
    public ResponseEntity<Page<SuggestedEventResponseDTO>> getMySuggestedEvents(
            @AuthenticationPrincipal CustomUserDetails principal,
            @ParameterObject Pageable pageable) {

        Long memberId = principal.getMemberDTO().getMemberId();
        Page<SuggestedEventResponseDTO> myEventPage = suggestedEventService.getMySuggestedEvents(memberId, pageable);
        return ResponseEntity.ok(myEventPage);
    }

    //랭킹 5위까지 조회
    @GetMapping("/top")
    @Operation(summary = "제안 행사 랭킹 TOP 5 조회", description = "'좋아요'가 많은 순서대로 상위 5개의 제안 행사를 조회합니다.")
    public ResponseEntity<List<SuggestedEventResponseDTO>> getTopSuggestedEvents(
            @AuthenticationPrincipal CustomUserDetails principal) {

        Long memberId = principal.getMemberDTO().getMemberId();
        List<SuggestedEventResponseDTO> topSuggestedEvents = suggestedEventService.getTopSuggestedEvents(memberId);
        return ResponseEntity.ok(topSuggestedEvents);
    }
}


