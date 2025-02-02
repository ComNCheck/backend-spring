package com.ComNCheck.ComNCheck.domain.employmentNotice.service;

import com.ComNCheck.ComNCheck.domain.employmentNotice.model.dto.response.EmploymentNoticeResponseDTO;
import com.ComNCheck.ComNCheck.domain.employmentNotice.model.dto.response.FastAPIEmploymentNoticeResponseListDTO;
import com.ComNCheck.ComNCheck.domain.employmentNotice.model.dto.response.PageEmploymentNoticeResponseDTO;
import com.ComNCheck.ComNCheck.domain.employmentNotice.model.entity.EmploymentNotice;
import com.ComNCheck.ComNCheck.domain.employmentNotice.repository.EmploymentNoticeRepository;
import com.ComNCheck.ComNCheck.domain.global.infrastructure.FastApiClient;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class EmploymentNoticeService {

    private final EmploymentNoticeRepository employmentNoticeRepository;
    private final FastApiClient fastApiClient;

    @Transactional
    public void syncEmploymentNotices() {
        FastAPIEmploymentNoticeResponseListDTO response = fastApiClient.fetchEmploymentNotices();

        List<EmploymentNotice> changeEmploymentNotices = new ArrayList<>();

        if(response != null && response.getNotices() != null) {
            for(EmploymentNoticeResponseDTO dto : response.getNotices()) {
                Optional<EmploymentNotice> findEmployment = employmentNoticeRepository
                        .findByEmploymentNoticeId(dto.getEmploymentNoticeId());
                if(findEmployment.isEmpty()) {
                    EmploymentNotice newEmploymentNotice = new EmploymentNotice(dto);
                    employmentNoticeRepository.save(newEmploymentNotice);
                    changeEmploymentNotices.add(newEmploymentNotice);
                } else {
                    EmploymentNotice existEmploymentNotice = findEmployment.get();
                    if (existEmploymentNotice.updateFromDto(dto)) {
                        employmentNoticeRepository.save(existEmploymentNotice);
                        changeEmploymentNotices.add(existEmploymentNotice);
                    }
                }
            }
        }
        if (!changeEmploymentNotices.isEmpty()) {
            // fcm 기능 구현
            System.out.println("알림 전송");
        }
    }

    public List<EmploymentNoticeResponseDTO> getAllEmploymentNotices() {
        return employmentNoticeRepository.findAll()
                .stream()
                .map(EmploymentNoticeResponseDTO::of)
                .toList();
    }

    public PageEmploymentNoticeResponseDTO getEmploymentNoticesPage(int page, int size) {
        List<EmploymentNotice> allEmploymentNotices = employmentNoticeRepository.findAll();
        allEmploymentNotices.sort(Comparator.comparing(EmploymentNotice::getDate).reversed());

        long totalElements = allEmploymentNotices.size();
        int totalPages = (int) Math.ceil((double) totalElements / size);

        if (page < 1) {
            page = 1;
        } else if (page > totalPages && totalPages > 0) {
            page = totalPages;
        }

        int zeroBasedPage = page - 1;
        int startIndex = zeroBasedPage * size;
        int endIndex = Math.min(startIndex + size, (int) totalElements);

        List<EmploymentNotice> pageList = (startIndex < endIndex)
                ? allEmploymentNotices.subList(startIndex, endIndex)
                : Collections.emptyList();

        List<EmploymentNoticeResponseDTO> content = pageList.stream()
                .map(EmploymentNoticeResponseDTO::of)
                .collect(Collectors.toList());

        return PageEmploymentNoticeResponseDTO.builder()
                .currentPage(page)
                .totalPages(totalPages)
                .totalElements(totalElements)
                .size(size)
                .content(content)
                .build();
    }
}
