package com.ComNCheck.ComNCheck.domain.employmentNotice.service;

import com.ComNCheck.ComNCheck.domain.employmentNotice.model.dto.response.EmploymentNoticeResponseDTO;
import com.ComNCheck.ComNCheck.domain.employmentNotice.model.dto.response.FastAPIEmploymentNoticeResponseListDTO;
import com.ComNCheck.ComNCheck.domain.employmentNotice.model.dto.response.PageEmploymentNoticeResponseDTO;
import com.ComNCheck.ComNCheck.domain.employmentNotice.model.entity.EmploymentNotice;
import com.ComNCheck.ComNCheck.domain.employmentNotice.repository.EmploymentNoticeRepository;
import com.ComNCheck.ComNCheck.domain.fcm.service.FcmService;
import com.ComNCheck.ComNCheck.domain.global.infrastructure.FastApiClient;
import com.ComNCheck.ComNCheck.domain.member.model.entity.Member;
import com.ComNCheck.ComNCheck.domain.member.repository.MemberRepository;
import com.google.firebase.messaging.FirebaseMessagingException;
import java.util.ArrayList;
import java.util.Collections;
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
    private final MemberRepository memberRepository;
    private final FcmService fcmService;

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
            List<Member> members = memberRepository.findByAlarmEmploymentNoticeTrue();

            if(!members.isEmpty()) {
                String title = "취업 공지사항";
                String body = "새로운 컴퓨터공학부 취업 글이 등록되었습니다.";

                for(Member member : members) {
                    if(!member.getFcmTokens().isEmpty()) {
                        member.getFcmTokens().forEach(fcmToken -> {
                            if(fcmToken.isValid() && fcmToken.getToken() != null
                                    && !fcmToken.getToken().isBlank()) {
                                try {
                                    fcmService.sendMessageToToken(fcmToken.getToken(), title,body);
                                } catch(FirebaseMessagingException e) { // 예외처리 이후 확인
                                    System.out.println("전송 실패");
                                }
                            }
                        });
                    }
                }
            }
        }
    }

    public List<EmploymentNoticeResponseDTO> getAllEmploymentNotices() {
        return employmentNoticeRepository.findAll()
                .stream()
                .map(EmploymentNoticeResponseDTO::of)
                .toList();
    }

    public PageEmploymentNoticeResponseDTO getEmploymentNoticesPage(int page, int size) {
        List<EmploymentNotice> allEmploymentNotices = employmentNoticeRepository.findAllOrderedById();


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
