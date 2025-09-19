package com.ComNCheck.ComNCheck.domain.majorEvent.service;

import com.ComNCheck.ComNCheck.domain.global.config.gcp.ImageManager;
import com.ComNCheck.ComNCheck.domain.global.config.validator.MemberValidator;
import com.ComNCheck.ComNCheck.domain.global.exception.EventException;
import com.ComNCheck.ComNCheck.domain.global.exception.MemberNotFoundException;
import com.ComNCheck.ComNCheck.domain.global.exception.TempEventException;
import com.ComNCheck.ComNCheck.domain.majorEvent.model.dto.converter.EventConverter;
import com.ComNCheck.ComNCheck.domain.majorEvent.model.dto.request.TempEventRequestDTO;
import com.ComNCheck.ComNCheck.domain.majorEvent.model.dto.response.CalendarResponseDTO;
import com.ComNCheck.ComNCheck.domain.majorEvent.model.dto.response.EventResponseDTO;
import com.ComNCheck.ComNCheck.domain.majorEvent.model.dto.response.TempEventResponseDTO;
import com.ComNCheck.ComNCheck.domain.majorEvent.model.entity.MajorEvent;
import com.ComNCheck.ComNCheck.domain.majorEvent.model.entity.TempMajorEvent;
import com.ComNCheck.ComNCheck.domain.majorEvent.model.entity.enums.EventType;
import com.ComNCheck.ComNCheck.domain.majorEvent.repository.MajorEventRepository;
import com.ComNCheck.ComNCheck.domain.majorEvent.repository.TempMajorEventRepository;
import com.ComNCheck.ComNCheck.domain.member.model.entity.Member;
import com.ComNCheck.ComNCheck.domain.member.model.entity.Role;
import com.ComNCheck.ComNCheck.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static com.ComNCheck.ComNCheck.domain.majorEvent.model.dto.response.TempEventResponseDTO.*;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CalenderService {
    private final MemberValidator memberValidator;
    private final ImageManager imageManager;

    private final TempMajorEventRepository tempMajorEventRepository;
    private final MajorEventRepository majorEventRepository;
    private final MemberRepository memberRepository;

    private final Set<Role> PRIVILEGED_ROLES = EnumSet.of(
            Role.ROLE_ADMIN,
            Role.ROLE_MAJOR_PRESIDENT,
            Role.ROLE_STUDENT_COUNCIL,
            Role.ROLE_GRADUATE_STUDENT
    );

    @Transactional
    public TempEventResponseDTO saveTempEvent(TempEventRequestDTO.TempEventCreate requestDto, Long memberId) {
        if(requestDto.getHostType() == null){
            throw new TempEventException("hostCategory 입력은 필수입니다.");
        }

        Member member = memberValidator.findMemberAndCheckRole(memberId);

        // 카테고리 지정했을 때
        String eventName;
        EventType category = requestDto.getCategory();

        if ((category == EventType.ETC || category == null) && !StringUtils.hasText(requestDto.getEventName())) {
            throw new TempEventException("기타(ETC) 카테고리를 선택했거나 카테고리가 없는 경우, 행사 이름(eventName)은 필수입니다.");
        }

        if (category != null && category != EventType.ETC) {
            eventName = requestDto.getCategory().getDisplayName();
        }else{
            eventName = requestDto.getEventName();
        }

        List<MultipartFile> cardNewsImages = requestDto.getCardNewsImages();

        List<String> imageUrls = new ArrayList<>();

        if (cardNewsImages != null && !cardNewsImages.isEmpty()) {
            imageUrls = imageManager.uploadImagesToGcs(cardNewsImages);
        }

        TempMajorEvent tempEvent = TempMajorEvent.builder()
        .eventName(eventName)
        .category(requestDto.getCategory())
        .hostType(requestDto.getHostType())
        .location(requestDto.getLocation())
        .notice(requestDto.getNotice())
        .googleFormLink(requestDto.getGoogleFormLink())
        .startDate(requestDto.getStartDate())
        .endDate(requestDto.getEndDate())
        .year(requestDto.getStartDate().getYear())
        .time(LocalTime.parse(requestDto.getTime()))
        .cardNewsImageUrls(imageUrls)
        .writer(member)
        .build();

        tempMajorEventRepository.save(tempEvent);
        return from(tempEvent);
    }

    @Transactional
    public TempEventResponseDTO updateTempEvent(Long tempEventId, TempEventRequestDTO.TempEventUpdate requestDto, Long memberId) {
        memberValidator.findMemberAndCheckRole(memberId);

        TempMajorEvent tempEvent = tempMajorEventRepository.findById(tempEventId)
                .orElseThrow(() -> new EventException("수정할 임시 행사를 찾을 수 없습니다."));

        List<String> oldImageUrls = tempEvent.getCardNewsImageUrls();
        List<String> newImageUrls = imageManager.uploadImagesToGcs(requestDto.getNewImages());

        List<String> finalImageUrls = new ArrayList<>();
        if (requestDto.getExistingImageUrls() != null) { // 유지할 사진
            finalImageUrls.addAll(requestDto.getExistingImageUrls());
        }
        finalImageUrls.addAll(newImageUrls); // 새로 업로드된 URL들

        //삭제 대상 이미지 URL을 찾아 GCS에서 삭제
        oldImageUrls.removeAll(finalImageUrls);
        imageManager.deleteImagesFromGcs(oldImageUrls);

        tempEvent.update(
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

        return from(tempEvent);
    }

    @Transactional
    public void deleteTempEvent(Long tempEventId, Long memberId) {
        memberValidator.findMemberAndCheckRole(memberId);

        TempMajorEvent tempEvent = tempMajorEventRepository.findById(tempEventId)
                .orElseThrow(() -> new EventException("삭제할 임시 행사를 찾을 수 없습니다."));

        // GCS에 업로드된 이미지 파일들을 먼저 삭제
        imageManager.deleteImagesFromGcs(tempEvent.getCardNewsImageUrls());

        tempMajorEventRepository.deleteById(tempEventId);
    }

    @Transactional
    public List<EventResponseDTO> submitAllTempEvents(TempEventRequestDTO.Fix requestDto, Long memberId) {
        memberValidator.findMemberAndCheckRole(memberId);

        List<Long> tempEventIds = requestDto.getTempEventIds();

        // 2. 제출할 임시 행사 ID 목록이 비어있는지 확인
        if (tempEventIds == null || tempEventIds.isEmpty()) {
            throw new EventException("제출할 행사를 선택해주세요.");
        }

        // 3. ID 목록으로 모든 임시 행사를 한 번에 조회
        List<TempMajorEvent> tempMajorEvents = tempMajorEventRepository.findAllById(tempEventIds);

        // MajorEvent로 복사 후 저장
        List<MajorEvent> majorEvents = tempMajorEvents.stream()
                .map(TempMajorEvent::toMajorEvent)
                .toList();

        majorEventRepository.saveAll(majorEvents);

        tempMajorEventRepository.deleteAll(tempMajorEvents);

        return EventConverter.toSubmitResponse(majorEvents);
    }

    public List<CalendarResponseDTO> getCalendarEvents(int year, int month, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("존재하지 않는 회원입니다."));

        LocalDate monthStart = LocalDate.of(year, month, 1);
        LocalDate monthEnd = monthStart.withDayOfMonth(monthStart.lengthOfMonth());

        // 확정된 행사 조회(공통)
        List<MajorEvent> fixedEvents = majorEventRepository.findByDateRange(monthStart, monthEnd);
        Stream<CalendarResponseDTO> fixedEventStream = fixedEvents.stream()
                .map(CalendarResponseDTO::from);

        // 권한이 있다면, 임시 저장된 행사도 추가로 조회
        if (PRIVILEGED_ROLES.contains(member.getRole())) {
            List<TempMajorEvent> tempEvents = tempMajorEventRepository.findByDateRange(monthStart, monthEnd);

            Stream<CalendarResponseDTO> tempEventStream = tempEvents.stream()
                    .map(CalendarResponseDTO::from);

            return Stream.concat(fixedEventStream, tempEventStream).toList();
        }

        return fixedEventStream.toList();
    }
}
