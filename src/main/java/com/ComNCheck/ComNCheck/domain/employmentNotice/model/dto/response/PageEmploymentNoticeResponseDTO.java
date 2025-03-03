package com.ComNCheck.ComNCheck.domain.employmentNotice.model.dto.response;

import com.ComNCheck.ComNCheck.domain.majorNotice.model.dto.response.MajorNoticeResponseDTO;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PageEmploymentNoticeResponseDTO {
    private int currentPage;
    private int totalPages;
    private long totalElements;
    private int size;
    private List<EmploymentNoticeResponseDTO> content;
}
