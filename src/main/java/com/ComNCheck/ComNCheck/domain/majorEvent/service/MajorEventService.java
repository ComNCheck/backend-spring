package com.ComNCheck.ComNCheck.domain.majorEvent.service;

import com.ComNCheck.ComNCheck.domain.fcm.service.FcmService;
import com.ComNCheck.ComNCheck.domain.global.config.gcp.ImageManager;
import com.ComNCheck.ComNCheck.domain.global.config.validator.MemberValidator;
import com.ComNCheck.ComNCheck.domain.global.exception.EventException;
import com.ComNCheck.ComNCheck.domain.global.exception.PostNotFoundException;
import com.ComNCheck.ComNCheck.domain.majorEvent.model.dto.converter.EventConverter;
import com.ComNCheck.ComNCheck.domain.majorEvent.model.dto.request.MajorEventRequestDTO;
import com.ComNCheck.ComNCheck.domain.majorEvent.model.dto.response.EventListResponseDTO;
import com.ComNCheck.ComNCheck.domain.majorEvent.model.dto.response.EventResponseDTO;
import com.ComNCheck.ComNCheck.domain.majorEvent.model.entity.MajorEvent;
import com.ComNCheck.ComNCheck.domain.majorEvent.model.entity.enums.EventType;
import com.ComNCheck.ComNCheck.domain.majorEvent.model.entity.enums.FilterCategory;
import com.ComNCheck.ComNCheck.domain.majorEvent.model.entity.enums.HostType;
import com.ComNCheck.ComNCheck.domain.majorEvent.repository.EventChecklistRepository;
import com.ComNCheck.ComNCheck.domain.majorEvent.repository.MajorEventRepository;
import com.ComNCheck.ComNCheck.domain.majorEvent.repository.TempMajorEventRepository;
import com.ComNCheck.ComNCheck.domain.member.repository.MemberRepository;
import com.google.cloud.storage.Storage;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MajorEventService {
    private final MemberValidator memberValidator;
    private final ImageManager imageManager;

    private final MajorEventRepository majorEventRepository;
    private final MemberRepository memberRepository;
    private final EventChecklistRepository eventChecklistRepository;
    private final TempMajorEventRepository tempMajorEventRepository;


    private final FcmService fcmService;
    @Value("${spring.cloud.gcp.storage.bucket}")
    private String bucketName;
    private final Storage storage;

//    @Transactional
//    public EventResponseDTO createMajorEvent(EventCreateRequestDTO requestDTO, Long writerId) {
//        Member writer = memberRepository.findByMemberId(writerId)
//                .orElseThrow(() -> new MemberNotFoundException("존재하지 않는 회원입니다."));
//
//        memberValidator.isCheckRole(writer);
//
//        List<String> imageUrls = imageManager.uploadImagesToGcs(requestDTO.getCardNewsImages());
//
//        LocalDate eventDate = requestDTO.getParsedDate();
//        LocalTime eventTime = requestDTO.getParsedTime();
//
//        MajorEvent majorEvent = MajorEvent.builder()
//                .writer(writer)
//                .eventName(requestDTO.getEventName())
//                .date(eventDate)
//                .time(eventTime)
//                .location(requestDTO.getLocation())
//                .notice(requestDTO.getNotice())
//                .googleFormLink(requestDTO.getGoogleFormLink())
//                .cardNewsImageUrls(imageUrls)
//                .build();
//
//        MajorEvent savedMajorEvent = majorEventRepository.save(majorEvent);
//
//        List<Member> members = memberRepository.findByAlarmMajorEventTrue();
//
//        if(!members.isEmpty()) {
//            String title = "공지사항";
//            String body = "새로운 과행사 글이 등록되었습니다.";
//
//            for(Member member : members) {
//                if(!member.getFcmTokens().isEmpty()) {
//                    member.getFcmTokens().forEach(fcmToken -> {
//                        if(fcmToken.isValid() && fcmToken.getToken() != null
//                                && !fcmToken.getToken().isBlank()) {
//                            try {
//                                fcmService.sendMessageToToken(fcmToken.getToken(), title,body);
//                            } catch(FirebaseMessagingException e) {
//                                System.out.println("전송 실패");
//                            }
//                        }
//                    });
//                }
//            }
//        }
//        return EventResponseDTO.of(savedMajorEvent);
//    }

    public EventResponseDTO.EventDTO getMajorEvent(Long majorEventId) {
        MajorEvent majorEvent = majorEventRepository.findById(majorEventId)
                .orElseThrow(() -> new PostNotFoundException("요청하신 학부 행사가 없습니다."));

        return EventResponseDTO.toEventDto(majorEvent);
    }
//
    public List<EventListResponseDTO.AllEventsDTO> getAllMajorEventsNotPassed(HostType hostCategory) {
        // 코드 상에서 정렬 보다는 디비에서 정렬하고 보내는 것이 더 효율적일꺼같음 추후 리펙토링 필요
        List<MajorEvent> all = majorEventRepository.findAll();

        LocalDate today = LocalDate.now();
        LocalTime currentTime = LocalTime.now();

//        List<MajorEvent> filtered = all.stream() // 기간 지난 행사 제외
//                .filter(e -> isNotPassed(e, today, currentTime))
//                .collect(Collectors.toList());

        List<MajorEvent> events = majorEventRepository.findNotPassedEventsByHostCategory(today, currentTime, hostCategory);

        return events.stream()
                .map(EventListResponseDTO::toMajorEventNotPassed)
                .collect(Collectors.toList());
//        all.sort(
//
//                Comparator
//                        .comparing(MajorEvent::getStartDate, Comparator.reverseOrder())  // 날짜 내림차순
//                        .thenComparing(MajorEvent::getTime, Comparator.reverseOrder()) // 시간 내림차순
//        );
//
//        return all.stream()
//                .map(EventListResponseDTO::toMajorEventNotPassed)
//                .collect(Collectors.toList());
    }

//    @Transactional
//    public EventResponseDTO updateMajorEvent(Long majorEventId, EventUpdateRequestDTO requestDTO, Long memberId) {
//        Member member = memberRepository.findByMemberId(memberId)
//                .orElseThrow(() -> new MemberNotFoundException("등록된 회원이 없습니다."));
//        isCheckRole(member);
//
//        MajorEvent majorEvent = majorEventRepository.findById(majorEventId)
//                .orElseThrow(() -> new PostNotFoundException("요청하신 학부 행사가 없습니다."));
//
//        LocalDate eventDate = requestDTO.getParsedDate();
//        LocalTime eventTime = requestDTO.getParsedTime();
//
//        majorEvent.updateEvent(
//                requestDTO.getEventName(),
//                eventDate,₩
//                eventTime,
//                requestDTO.getLocation(),
//                requestDTO.getNotice(),
//                requestDTO.getGoogleFormLink()
//        );
//        if (requestDTO.getCardNewsImages() != null && !requestDTO.getCardNewsImages().isEmpty()) {
//            List<String> newImageUrls = uploadImagesToGcs(requestDTO.getCardNewsImages());
//            majorEvent.updateCardNewsImages(newImageUrls);
//        }
//
//        return EventResponseDTO.of(majorEvent);
//    }

//    @Transactional
//    public void deleteMajorEvent(Long majorEventId, Long memberId) {
//        Member member = memberRepository.findByMemberId(memberId)
//                .orElseThrow(() -> new MemberNotFoundException("등록된 회원이 없습니다."));
//        isCheckRole(member);
//
//        MajorEvent majorEvent = majorEventRepository.findById(majorEventId)
//                .orElseThrow(() -> new PostNotFoundException("요청하신 학부 행사가 없습니다."));
//        majorEventRepository.delete(majorEvent);
//    }

    public List<EventListResponseDTO.EventByYearResponseDTO> searchEvents(Integer year, FilterCategory category, Long memberId){
        memberValidator.findMemberAndCheckRole(memberId);

        boolean isYearExists = (year != null);
        boolean isCategoryExists = (category != null);

//        if (!isYearExists && !isCategoryExists) {
//            throw new EventException("조회 조건(연도 또는 카테고리)이 반드시 필요합니다.");
//        }else
        if (isYearExists && isCategoryExists) {
            throw new EventException("연도별 조회와 카테고리별 조회는 함께 사용할 수 없습니다.");
        }

        List<MajorEvent> majorEvents;

        if (isYearExists) {
            majorEvents = majorEventRepository.search(year, null);
        } else if(isCategoryExists){
            List<EventType> categories = EventType.getByFilterCategory(category);
            if(categories.isEmpty()){
                return Collections.emptyList();
            }
            majorEvents = majorEventRepository.search(null, categories);
        } else{
            majorEvents = majorEventRepository.search(null, null);
        }

        return EventConverter.toYearlyEventResponse(majorEvents);
    }

    public EventResponseDTO.Count countAllEvents(Long memberId) {
        memberValidator.findMemberAndCheckRole(memberId);

        return EventConverter.toCountResponse(majorEventRepository.count());
    }

    @Transactional
    public EventResponseDTO updateMajorEvent(Long eventId, MajorEventRequestDTO.MajorEventUpdate requestDto, Long memberId) {
        memberValidator.findMemberAndCheckRole(memberId);

        MajorEvent majorEvent = majorEventRepository.findById(eventId)
                .orElseThrow(() -> new EventException("수정할 행사를 찾을 수 없습니다."));

        List<String> oldImageUrls = new ArrayList<>(majorEvent.getCardNewsImageUrls());
        List<String> newImageUrls = imageManager.uploadImagesToGcs(requestDto.getNewImages());

        List<String> finalImageUrls = new ArrayList<>();
        if (requestDto.getExistingImageUrls() != null) {
            finalImageUrls.addAll(requestDto.getExistingImageUrls());
        }
        finalImageUrls.addAll(newImageUrls);

        oldImageUrls.removeAll(finalImageUrls);
        imageManager.deleteImagesFromGcs(oldImageUrls);

        majorEvent.update(
                requestDto.getEventName(),
                requestDto.getCategory(),
                requestDto.getHostType(),
                requestDto.getStartDate(),
                requestDto.getEndDate(),
                LocalTime.parse(requestDto.getTime()),
                requestDto.getLocation(),
                requestDto.getNotice(),
                requestDto.getGoogleFormLink(),
                finalImageUrls
        );

        return EventResponseDTO.from(majorEvent);
    }

    @Transactional
    public void deleteMajorEvent(Long eventId, Long memberId) {
        memberValidator.findMemberAndCheckRole(memberId);

        MajorEvent majorEvent = majorEventRepository.findById(eventId)
                .orElseThrow(() -> new EventException("삭제할 행사를 찾을 수 없습니다."));

        imageManager.deleteImagesFromGcs(majorEvent.getCardNewsImageUrls());

        majorEventRepository.delete(majorEvent);
    }
}
