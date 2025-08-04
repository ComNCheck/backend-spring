package com.ComNCheck.ComNCheck.domain.anotherEvent.service;

import com.ComNCheck.ComNCheck.domain.fcm.service.FcmService;
import com.ComNCheck.ComNCheck.domain.global.exception.ForbiddenException;
import com.ComNCheck.ComNCheck.domain.global.exception.MemberNotFoundException;
import com.ComNCheck.ComNCheck.domain.global.exception.PostNotFoundException;
import com.ComNCheck.ComNCheck.domain.anotherEvent.model.dto.request.EventCreateRequestDTO;
import com.ComNCheck.ComNCheck.domain.anotherEvent.model.dto.request.EventUpdateRequestDTO;
import com.ComNCheck.ComNCheck.domain.anotherEvent.model.dto.response.EventListResponseDTO;
import com.ComNCheck.ComNCheck.domain.anotherEvent.model.dto.response.EventResponseDTO;
import com.ComNCheck.ComNCheck.domain.anotherEvent.model.entity.AnotherEvent;
import com.ComNCheck.ComNCheck.domain.anotherEvent.repository.AnotherEventRepository;
import com.ComNCheck.ComNCheck.domain.member.model.entity.Member;
import com.ComNCheck.ComNCheck.domain.member.model.entity.Role;
import com.ComNCheck.ComNCheck.domain.member.repository.MemberRepository;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.firebase.messaging.FirebaseMessagingException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AnotherEventService {

    private final AnotherEventRepository anotherEventRepository;
    private final MemberRepository memberRepository;
    private final FcmService fcmService;

    @Value("${spring.cloud.gcp.storage.bucket}")
    private String bucketName;

    private final Storage storage;

    @Transactional
    public EventResponseDTO createAnotherEvent(EventCreateRequestDTO requestDTO, Long writerId) {
        Member writer = memberRepository.findByMemberId(writerId)
                .orElseThrow(() -> new MemberNotFoundException("존재하지 않는 회원입니다."));

        isCheckRole(writer);

        List<String> imageUrls = uploadImagesToGcs(requestDTO.getCardNewsImages());

        LocalDate eventDate = requestDTO.getParsedDate();
        LocalTime eventTime = requestDTO.getParsedTime();
        AnotherEvent anotherEvent = AnotherEvent.builder()
                .writer(writer)
                .eventName(requestDTO.getEventName())
                .date(eventDate)
                .time(eventTime)
                .location(requestDTO.getLocation())
                .notice(requestDTO.getNotice())
                .googleFormLink(requestDTO.getGoogleFormLink())
                .cardNewsImageUrls(imageUrls)
                .build();
        AnotherEvent savedAnotherEvent = anotherEventRepository.save(anotherEvent);

        String title = "새로운 행사 등록";
        String body = requestDTO.getEventName() + " 행사가 등록되었습니다.";
        memberRepository.findAll().forEach(member -> {
            member.getFcmTokens().forEach(fcmToken -> {
                if (fcmToken.isValid() && fcmToken.getToken() != null && !fcmToken.getToken().isBlank()) {
                    try {
                        fcmService.sendMessageToToken(fcmToken.getToken(), title, body);
                    } catch (FirebaseMessagingException e) {
                        System.out.println("전송 실패");
                    }
                }
            });
        });

        return EventResponseDTO.of(savedAnotherEvent);
    }

    @Transactional
    public void deleteAnotherEvent(Long anotherEventId, Long memberId) {
        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new MemberNotFoundException("등록된 회원이 없습니다."));
        isCheckRole(member);

        AnotherEvent anotherEvent = anotherEventRepository.findById(anotherEventId)
                .orElseThrow(() -> new PostNotFoundException("요청하신 행사가 없습니다."));
        anotherEventRepository.delete(anotherEvent);
    }

    public EventResponseDTO getAnotherEvent(Long anotherEventId) {
        AnotherEvent anotherEvent = anotherEventRepository.findById(anotherEventId)
                .orElseThrow(() -> new PostNotFoundException("요청하신 행사가 없습니다."));
        return EventResponseDTO.of(anotherEvent);
    }

    public List<EventListResponseDTO> getAllAnotherEventsNotPassed() {
        List<AnotherEvent> all = anotherEventRepository.findAll();

        LocalDate today = LocalDate.now();
        LocalTime currentTime = LocalTime.now();

        all.sort(
            Comparator
                .comparing(AnotherEvent::getDate, Comparator.reverseOrder())
                .thenComparing(AnotherEvent::getTime, Comparator.reverseOrder())
        );

        return all.stream()
                  .map(EventListResponseDTO::of)
                  .collect(Collectors.toList());
    }

    @Transactional
    public EventResponseDTO updateAnotherEvent(Long anotherEventId, EventUpdateRequestDTO requestDTO, Long memberId) {
        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new MemberNotFoundException("등록된 회원이 없습니다."));
        isCheckRole(member);

        AnotherEvent anotherEvent = anotherEventRepository.findById(anotherEventId)
                .orElseThrow(() -> new PostNotFoundException("요청하신 행사가 없습니다."));

        LocalDate eventDate = requestDTO.getParsedDate();
        LocalTime eventTime = requestDTO.getParsedTime();

        anotherEvent.updateEvent(
            requestDTO.getEventName(),
            eventDate,
            eventTime,
            requestDTO.getLocation(),
            requestDTO.getNotice(),
            requestDTO.getGoogleFormLink()
        );
        if (requestDTO.getCardNewsImages() != null && !requestDTO.getCardNewsImages().isEmpty()) {
            List<String> newImageUrls = uploadImagesToGcs(requestDTO.getCardNewsImages());
            anotherEvent.updateCardNewsImages(newImageUrls);
        }

        return EventResponseDTO.of(anotherEvent);
    }

    private List<String> uploadImagesToGcs(List<MultipartFile> images) {
        if (images == null || images.isEmpty()) {
            return new ArrayList<>();
        }
        List<String> uploadUrls = new ArrayList<>();
        for (MultipartFile file : images) {
            try {
                String uuid = UUID.randomUUID().toString();
                String contentType = file.getContentType() != null ? file.getContentType() : "application/octet-stream";
                BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, uuid)
                                            .setContentType(contentType)
                                            .build();
                storage.create(blobInfo, file.getInputStream());
                uploadUrls.add("https://storage.googleapis.com/" + bucketName + "/" + uuid);
            } catch (IOException e) {
                throw new RuntimeException("이미지 업로드 실패", e);
            }
        }
        return uploadUrls;
    }

    public void isCheckRole(Member member) {
        Role role = member.getRole();
        if (role != Role.ROLE_ADMIN
         && role != Role.ROLE_MAJOR_PRESIDENT
         && role != Role.ROLE_STUDENT_COUNCIL) {
            throw new ForbiddenException("접근 권한이 없습니다.");
        }
    }
}
