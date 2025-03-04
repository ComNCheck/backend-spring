package com.ComNCheck.ComNCheck.domain.majorNotice.service;

import com.ComNCheck.ComNCheck.domain.fcm.service.FcmService;
import com.ComNCheck.ComNCheck.domain.global.infrastructure.FastApiClient;
import com.ComNCheck.ComNCheck.domain.majorNotice.model.dto.response.FastAPIMajorNoticesResponseListDTO;
import com.ComNCheck.ComNCheck.domain.majorNotice.model.dto.response.MajorNoticeResponseDTO;
import com.ComNCheck.ComNCheck.domain.majorNotice.model.dto.response.PageMajorNoticeResponseDTO;
import com.ComNCheck.ComNCheck.domain.majorNotice.model.entity.MajorNotice;
import com.ComNCheck.ComNCheck.domain.majorNotice.repository.MajorNoticeRepository;
import com.ComNCheck.ComNCheck.domain.member.model.entity.Member;
import com.ComNCheck.ComNCheck.domain.member.repository.MemberRepository;
import com.google.firebase.messaging.FirebaseMessagingException;
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
public class MajorNoticeService {

    private final FastApiClient fastApiClient;
    private final MajorNoticeRepository majorNoticeRepository;
    private final MemberRepository memberRepository;
    private final FcmService fcmService;

    @Transactional
    public void syncMajorNotices() {
        FastAPIMajorNoticesResponseListDTO response = fastApiClient.fetchMajorNotices();

        List<MajorNotice> changeMajorNotices = new ArrayList<>();

        if(response != null && response.getNotices() != null) {
            for(MajorNoticeResponseDTO dto : response.getNotices()) {
                Optional<MajorNotice> findNotice = majorNoticeRepository.findByNoticeId(dto.getNoticeId());
                if(findNotice.isEmpty()) {
                    MajorNotice newMajorNotice = new MajorNotice(dto);
                    majorNoticeRepository.save(newMajorNotice);
                    changeMajorNotices.add(newMajorNotice);
                } else {
                    MajorNotice existingMajorNotice = findNotice.get();
                    if (existingMajorNotice.updateFromDto(dto)) {
                        majorNoticeRepository.save(existingMajorNotice);
                        changeMajorNotices.add(existingMajorNotice);
                    }
                }
            }
        }
        if(!changeMajorNotices.isEmpty()) {
            // fcm 기능 구현
            System.out.println("알림 전송");
            List<Member> members = memberRepository.findByAlarmMajorNoticeTrue();

            if(!members.isEmpty()) {
                String title = "전공 공지사항";
                String body = "새로운 컴퓨터공학부 공지사항 글이 등록되었습니다.";

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

    public List<MajorNoticeResponseDTO> getAllMajorNotices() {
        return majorNoticeRepository.findAll()
                .stream()
                .map(MajorNoticeResponseDTO::of)
                .toList();
    }

    public PageMajorNoticeResponseDTO getMajorNoticesPage(int page, int size) {
        List<MajorNotice> allNotices = majorNoticeRepository.findAllOrderedById();


        long totalElements = allNotices.size();
        int totalPages = (int) Math.ceil((double) totalElements / size);

        if (page < 1) {
            page = 1;
        } else if (page > totalPages && totalPages > 0) {
            page = totalPages;
        }

        int zeroBasedPage = page - 1;
        int startIndex = zeroBasedPage * size;
        int endIndex = Math.min(startIndex + size, (int) totalElements);

        List<MajorNotice> pageList = (startIndex < endIndex)
                ? allNotices.subList(startIndex, endIndex)
                : Collections.emptyList();

        List<MajorNoticeResponseDTO> content = pageList.stream()
                .map(MajorNoticeResponseDTO::of)
                .collect(Collectors.toList());

        return PageMajorNoticeResponseDTO.builder()
                .currentPage(page)
                .totalPages(totalPages)
                .totalElements(totalElements)
                .size(size)
                .content(content)
                .build();
    }
}
