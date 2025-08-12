package com.ComNCheck.ComNCheck.domain.anotherEvent.model.dto.response;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PagedEventListResponseDTO {
    private int currentPage;
    private int totalPages;
    private long totalElements;
    private int size;
    private List<EventListResponseDTO> content;

    public PagedEventListResponseDTO(int currentPage, int totalPages, long totalElements,
                                     int size, List<EventListResponseDTO> content)
    {
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.size = size;
        this.content = content;
    }
}
