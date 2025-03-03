package com.ComNCheck.ComNCheck.domain.employmentNotice.controller;

import com.ComNCheck.ComNCheck.domain.employmentNotice.model.dto.response.EmploymentNoticeResponseDTO;
import com.ComNCheck.ComNCheck.domain.employmentNotice.model.dto.response.PageEmploymentNoticeResponseDTO;
import com.ComNCheck.ComNCheck.domain.employmentNotice.service.EmploymentNoticeService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.xml.bind.annotation.XmlType.DEFAULT;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/employment/notices")
@RestController
public class EmploymentController {

    private final EmploymentNoticeService employmentNoticeService;

    @GetMapping
    @Operation(summary = "취업정보 게시판 목록 조회", description = "취업정보 게시판 목록을 조회한다.")
    public ResponseEntity<List<EmploymentNoticeResponseDTO>> getAllEmploymentNotice() {
        List<EmploymentNoticeResponseDTO> lists = employmentNoticeService.getAllEmploymentNotices();
        return ResponseEntity.ok(lists);
    }

    @GetMapping("/pages")
    @Operation(summary = "취업정보 게시판 목록 조회(페이지네이션)", description = "페이지네이션으로 취업정보 게시판 목록을 조회한다.")
    public ResponseEntity<PageEmploymentNoticeResponseDTO> getEmploymentNoticePage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PageEmploymentNoticeResponseDTO pageResponse = employmentNoticeService.getEmploymentNoticesPage(page, size);
        return ResponseEntity.ok(pageResponse);

    }
}
