package com.ComNCheck.ComNCheck.domain.roleChange.controller;

import com.ComNCheck.ComNCheck.domain.member.model.entity.Role;
import com.ComNCheck.ComNCheck.domain.roleChange.model.dto.request.RoleChangeRequestDTO;
import com.ComNCheck.ComNCheck.domain.roleChange.model.dto.response.ApprovedRoleListDTO;
import com.ComNCheck.ComNCheck.domain.roleChange.model.dto.response.RoleChangeListDTO;
import com.ComNCheck.ComNCheck.domain.roleChange.model.dto.response.RoleChangeResponseDTO;
import com.ComNCheck.ComNCheck.domain.roleChange.service.RoleChangeRequestService;
import com.ComNCheck.ComNCheck.domain.security.oauth.CustomOAuth2Member;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RequestMapping("/api/v1/role-change-requests")
@RequiredArgsConstructor
@RestController
public class RoleChangeController {

    private final RoleChangeRequestService roleChangeRequestService;

    @PostMapping
    @Operation(summary = "학생회 등급 신청", description = "학생회 인원이 학생회 등급으로 신청한다")
    public ResponseEntity<RoleChangeResponseDTO> createRoleChangeRequest(
            @RequestBody RoleChangeRequestDTO requestDTO,
            Authentication authentication) {
        CustomOAuth2Member principal = (CustomOAuth2Member) authentication.getPrincipal();
        Long memberId = principal.getMemberDTO().getMemberId();
        RoleChangeResponseDTO createDTO = roleChangeRequestService.createRoleChangeRequest(memberId, requestDTO);
        URI location = URI.create("api/role-change-requests/" + createDTO.getRequestId());
        return ResponseEntity.created(location).body(createDTO);
    }

    @GetMapping
    @Operation(summary = "학생회 등급 신청 목록 조회", description = "학생회 등급 신청 목록을 조회한다.")
    public ResponseEntity<List<RoleChangeListDTO>> getAllRequest(Authentication authentication) {
        CustomOAuth2Member principal = (CustomOAuth2Member) authentication.getPrincipal();
        Long memberId = principal.getMemberDTO().getMemberId();
        List<RoleChangeListDTO> response = roleChangeRequestService.getAllRequests(memberId);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/{requestId}")
    @Operation(summary = "특정 학생회 등급 신청 조회", description = "특정 학생회 등급 신청을 조회한다.")
    public ResponseEntity<RoleChangeResponseDTO> getRequestDetail(@PathVariable Long requestId,
            Authentication authentication) {
        CustomOAuth2Member principal = (CustomOAuth2Member) authentication.getPrincipal();
        Long memberId = principal.getMemberDTO().getMemberId();
        RoleChangeResponseDTO responseDTO = roleChangeRequestService.getRequestDetail(requestId, memberId);
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("{requestId}/approve")
    @Operation(summary = "학생회 등급 신청 승인", description = "과회장 or 관리자가 학생회 등급 신청을 승인한다.")
    public ResponseEntity<String> approveRequest(@PathVariable Long requestId
            , Authentication authentication) {
        CustomOAuth2Member principal = (CustomOAuth2Member) authentication.getPrincipal();
        Long memberId = principal.getMemberDTO().getMemberId();
        roleChangeRequestService.approveRequest(requestId, memberId);
        return ResponseEntity.ok("승인완료");
    }

    @DeleteMapping("/{requestId}")
    @Operation(summary = "특정 학생회 등급 신청 요청 삭제", description = "요청온 학생회 등급 신청 요청을 삭제한다.")
    public ResponseEntity<String> deleteRequest(@PathVariable Long requestId,
                                                Authentication authentication) {
        CustomOAuth2Member principal = (CustomOAuth2Member) authentication.getPrincipal();
        Long memberId = principal.getMemberDTO().getMemberId();
        roleChangeRequestService.deleteRequest(requestId, memberId);
        return ResponseEntity.noContent().build();
    }

}
