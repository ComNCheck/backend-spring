package com.ComNCheck.ComNCheck.domain.developerQuestion.controller;


import com.ComNCheck.ComNCheck.domain.developerQuestion.model.dto.request.DeveloperQuestionRequestDTO;
import com.ComNCheck.ComNCheck.domain.developerQuestion.model.dto.response.DeveloperQuestionResponseDTO;
import com.ComNCheck.ComNCheck.domain.developerQuestion.service.DeveloperQuestionService;
import com.ComNCheck.ComNCheck.domain.security.oauth.CustomOAuth2Member;
import io.swagger.v3.oas.annotations.Operation;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("api/v1/developer/questions")
@RequiredArgsConstructor
@RestController
public class DeveloperQuestionController {

    private final DeveloperQuestionService developerQuestionService;

    @PostMapping
    @Operation(summary = "개발자 질문 게시글 작성", description = "개발자에게 질문글을 작성할 수 있다.")
    public ResponseEntity<DeveloperQuestionResponseDTO> createDeveloperQuestion(
            @RequestBody DeveloperQuestionRequestDTO requestDTO,
            Authentication authentication
            ) {
        CustomOAuth2Member principal = (CustomOAuth2Member) authentication.getPrincipal();
        Long memberId = principal.getMemberDTO().getMemberId();
        DeveloperQuestionResponseDTO createdDTO = developerQuestionService
                .createDeveloperQuestion(memberId, requestDTO);
        URI location = URI.create("api/v1/developer/questions/" + createdDTO.getId());
        return ResponseEntity.created(location).body(createdDTO);
    }

    @GetMapping("/{developerQuestionId}")
    @Operation(summary = "특정 개발자 질문 게시글 조회", description = "특정 개발자 질문 게시글 조회한다.")
    public ResponseEntity<DeveloperQuestionResponseDTO> getDeveloperQuestion(
            @PathVariable Long developerQuestionId
    ) {
        DeveloperQuestionResponseDTO responseDTO = developerQuestionService.getDeveloperQuestion(developerQuestionId);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping
    @Operation(summary = "개발자 질문 게시글 목록 조회", description = "개발자 질문 게시글 목록 조회한다.")
    public ResponseEntity<List<DeveloperQuestionResponseDTO>> getAllDeveloperQuestions() {
        List<DeveloperQuestionResponseDTO> questionList = developerQuestionService.getAllQuestion();
        return ResponseEntity.ok(questionList);
    }

    @PutMapping("/{developerQuestionId}")
    @Operation(summary = "개발자 질문 게시글 수정", description = "개발자 질문 게시글을 수정한다.")
    public ResponseEntity<DeveloperQuestionResponseDTO> updateDeveloperQuestion(
            @PathVariable Long developerQuestionId,
            @RequestBody DeveloperQuestionResponseDTO requestDTO,
            Authentication authentication
    ) {
        CustomOAuth2Member principal = (CustomOAuth2Member) authentication.getPrincipal();
        Long writerId = principal.getMemberDTO().getMemberId();
        DeveloperQuestionResponseDTO updatedDTO =
                developerQuestionService.updateDeveloperQuestion(developerQuestionId, requestDTO, writerId);

        return ResponseEntity.ok(updatedDTO);
    }

    @DeleteMapping("/{developerQuestionId}")
    @Operation(summary = "개발자 질문 게시글 삭제", description = "개발자 질문 게시글을 삭제한다.")
    public ResponseEntity<Void> deleteDeveloperQuestion(
            @PathVariable Long developerQuestionId,
            Authentication authentication
    ) {
        CustomOAuth2Member principal = (CustomOAuth2Member) authentication.getPrincipal();
        Long writerId = principal.getMemberDTO().getMemberId();
        developerQuestionService.deleteDeveloperQuestion(developerQuestionId, writerId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/my")
    @Operation(summary = "내가 쓴 개발자 질문 게시글 보기", description = "내가 쓴 글만 조회한다.")
    public ResponseEntity<List<DeveloperQuestionResponseDTO>> getAllMyDeveloperQuestion(
            Authentication authentication
    ) {
        CustomOAuth2Member principal = (CustomOAuth2Member) authentication.getPrincipal();
        Long writerId = principal.getMemberDTO().getMemberId();
        List<DeveloperQuestionResponseDTO> developerQuestions = developerQuestionService.getAllMyDeveloperQuestion(writerId);
        return ResponseEntity.ok(developerQuestions);
    }
}
