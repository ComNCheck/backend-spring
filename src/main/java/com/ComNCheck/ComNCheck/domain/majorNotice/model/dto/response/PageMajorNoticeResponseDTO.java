package com.ComNCheck.ComNCheck.domain.majorNotice.model.dto.response;


import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PageMajorNoticeResponseDTO {
    private int currentPage;
    private int totalPages;
    private long totalElements;
    private int size;
    private List<MajorNoticeResponseDTO> content;
}
