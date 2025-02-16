package com.ComNCheck.ComNCheck.domain.majorQuestion.controller;

import com.ComNCheck.ComNCheck.domain.majorQuestion.model.dto.request.AnswerRequestDTO;
import com.ComNCheck.ComNCheck.domain.majorQuestion.model.dto.response.AnswerResponseDTO;
import com.ComNCheck.ComNCheck.domain.majorQuestion.service.AnswerService;
import com.ComNCheck.ComNCheck.domain.security.oauth.CustomOAuth2Member;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/major/answers")
@RequiredArgsConstructor
public class AnswerController {

    private final AnswerService answerService;
    @PostMapping
    @Operation(summary = "fAQ 댓글 작성", description = "학생회권한만 댓글을 달 수 있다.")
    public ResponseEntity<AnswerResponseDTO> createOrUpdateAnswer(
            @RequestBody AnswerRequestDTO requestDTO,
            Authentication authentication
    ) {
        CustomOAuth2Member principal = (CustomOAuth2Member) authentication.getPrincipal();
        Long writerId = principal.getMemberDTO().getMemberId();

        AnswerResponseDTO responseDTO = answerService.createOrUpdateAnswer(requestDTO, writerId);
        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("/{answerId}")
    @Operation(summary = "fAQ 댓글 수정", description = "학생회권한만 댓글 작성자의 관계없이 댓글 수정이 가능하다.")
    public ResponseEntity<AnswerResponseDTO> updateAnswer(
            @PathVariable Long answerId,
            @RequestBody AnswerRequestDTO answerRequestDTO,
            Authentication authentication
    ) {
        CustomOAuth2Member principal = (CustomOAuth2Member) authentication.getPrincipal();
        Long writerId = principal.getMemberDTO().getMemberId();
        AnswerResponseDTO responseDTO = answerService.updateAnswer(answerId, answerRequestDTO.getContent(), writerId);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{answerId}")
    @Operation(summary = "fAQ 댓글 삭제", description = "학생회권한만 작성자의 관계없이 댓글 삭제가 가능하다")
    public ResponseEntity<Void> deleteAnswer(
            @PathVariable Long answerId,
            Authentication authentication
    ) {
        CustomOAuth2Member principal = (CustomOAuth2Member) authentication.getPrincipal();
        Long writerId = principal.getMemberDTO().getMemberId();
        answerService.deleteAnswer(answerId, writerId);
        return ResponseEntity.noContent().build();
    }
}
