package com.ComNCheck.ComNCheck.domain.member.controller;

import com.ComNCheck.ComNCheck.domain.member.model.dto.response.MemberInformationResponseDTO;
import com.ComNCheck.ComNCheck.domain.member.model.dto.response.PresidentCouncilResponseDTO;
import com.ComNCheck.ComNCheck.domain.member.service.CustomOAuthMemberService;
import com.ComNCheck.ComNCheck.domain.member.service.MemberService;
import com.ComNCheck.ComNCheck.domain.security.handler.CustomSuccessHandler;
import com.ComNCheck.ComNCheck.domain.security.oauth.CustomOAuth2Member;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
public class MemberController {
    private final MemberService memberService;
    private final CustomSuccessHandler customSuccessHandler;

    @PostMapping("/student/number")
    @Operation(summary = "학번 등록", description = "모바일 학생증으로 학번을 등록한다.")
    public ResponseEntity<MemberInformationResponseDTO> registerStudentNumber(
            @RequestParam("studentCardImage") MultipartFile studentCardImage,
            Authentication authentication) {
        CustomOAuth2Member principal = (CustomOAuth2Member) authentication.getPrincipal();
        Long memberId = principal.getMemberDTO().getMemberId();
        MemberInformationResponseDTO responseDTO = memberService.registerStudentNumber(memberId, studentCardImage);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/members/president-council")
    @Operation(summary = "학생회 목록 조회", description = "학생회 목록을 조회한다.")
    public ResponseEntity<PresidentCouncilResponseDTO> getPresidentCouncilList() {
        PresidentCouncilResponseDTO responseDTO = memberService.getPresidentAndCouncils();
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping
    @Operation(summary = "본인 정보 조회", description = "로그인 이후, 학번 변동 이후 본인 정보를 조회한다.")
    public ResponseEntity<MemberInformationResponseDTO> getMemberInformation(Authentication authentication) {
        CustomOAuth2Member principal = (CustomOAuth2Member) authentication.getPrincipal();
        Long memberId = principal.getMemberDTO().getMemberId();
        MemberInformationResponseDTO responseDTO = memberService.getMemberInformation(memberId);
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃", description = "쿠키의 jwt를 강제로 만료시켜 로그아웃 시킨다.")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        customSuccessHandler.clearAuthenticationSuccess(request, response);
        return ResponseEntity.ok("로그아웃 성공");
    }

    @PostMapping("/alarm/major/events")
    @Operation(summary = "과행사 알람 켜기 및 끄기", description = "과행사 알람이 오는 것을 켜거나 끌 수 있다.")
    public ResponseEntity<String> changeAlarmMajorEvent(Authentication authentication) {
        CustomOAuth2Member principal = (CustomOAuth2Member) authentication.getPrincipal();
        Long memberId = principal.getMemberDTO().getMemberId();
        memberService.changeAlarmMajorEvent(memberId);
        return ResponseEntity.ok("과행사 알람 변경");
    }

    @PostMapping("/alarm/major/notices")
    @Operation(summary = "과공지 알람 켜기 및 끄기", description = "과행사 알람이 오는 것을 켜거나 끌 수 있다.")
    public ResponseEntity<String> changeAlarmMajorNotice(Authentication authentication) {
        CustomOAuth2Member principal = (CustomOAuth2Member) authentication.getPrincipal();
        Long memberId = principal.getMemberDTO().getMemberId();
        memberService.changeAlarmMajorNotice(memberId);
        return ResponseEntity.ok("과행사 알람 변경");
    }

    @PostMapping("/alarm/employment/notices")
    @Operation(summary = "과 취업정보 알람 켜기 및 끄기", description = "과행사 알람이 오는 것을 켜거나 끌 수 있다.")
    public ResponseEntity<String> changeAlarmEmploymentNotice(Authentication authentication) {
        CustomOAuth2Member principal = (CustomOAuth2Member) authentication.getPrincipal();
        Long memberId = principal.getMemberDTO().getMemberId();
        memberService.changeAlarmEmploymentNotice(memberId);
        return ResponseEntity.ok("과행사 알람 변경");
    }
}
