package com.ComNCheck.ComNCheck.domain.majorEvent.service;

import com.ComNCheck.ComNCheck.domain.global.exception.MemberException;
import com.ComNCheck.ComNCheck.domain.global.exception.MemberNotFoundException;
import com.ComNCheck.ComNCheck.domain.global.exception.SuggestedEventException;
import com.ComNCheck.ComNCheck.domain.majorEvent.model.dto.request.SuggestedEventRequestDTO;
import com.ComNCheck.ComNCheck.domain.majorEvent.model.dto.response.SuggestedEventResponseDTO;
import com.ComNCheck.ComNCheck.domain.majorEvent.model.entity.EventLike;
import com.ComNCheck.ComNCheck.domain.majorEvent.model.entity.SuggestedEvent;
import com.ComNCheck.ComNCheck.domain.majorEvent.repository.EventLikeRepository;
import com.ComNCheck.ComNCheck.domain.majorEvent.repository.SuggestedEventRepository;
import com.ComNCheck.ComNCheck.domain.member.model.entity.Member;
import com.ComNCheck.ComNCheck.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SuggestedEventService {

    private final SuggestedEventRepository suggestedEventRepository;
    private final EventLikeRepository eventLikeRepository;
    private final MemberRepository memberRepository;

    //행사 신청
    @Transactional(readOnly = false)
    public SuggestedEventResponseDTO createSuggestedEvent(SuggestedEventRequestDTO.Create requestDto, Long memberId) {
        Member proposer = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("사용자를 찾을 수 없습니다."));

        SuggestedEvent newEvent = SuggestedEvent.builder()
                .eventName(requestDto.getEventName())
                .description(requestDto.getDescription())
                .messageToCouncil(requestDto.getMessageToCouncil())
                .proposer(proposer)
                .build();

        SuggestedEvent savedEvent = suggestedEventRepository.save(newEvent);

        return SuggestedEventResponseDTO.from(savedEvent, false); // 응답 DTO로 변환하여 반환
    }

    //행사 수정
    @Transactional(readOnly = false)
    public SuggestedEventResponseDTO updateSuggestedEvent(Long eventId, SuggestedEventRequestDTO.SuggestedEventUpdate requestDto, Long memberId) {
        SuggestedEvent event = suggestedEventRepository.findById(eventId)
                .orElseThrow(() -> new SuggestedEventException("수정할 행사를 찾을 수 없습니다."));

        // 제안자 본인만 수정 가능
        if (!event.getProposer().getMemberId().equals(memberId)) {
            throw new SuggestedEventException("이 제안행사를 수정할 권한이 없습니다.");
        }

        event.update(
                requestDto.getEventName(),
                requestDto.getDescription(),
                requestDto.getMessageToCouncil()
        );

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("존재하지 않는 유저입니다."));
        boolean isLiked = eventLikeRepository.existsByMemberAndSuggestedEvent(member, event);

        return SuggestedEventResponseDTO.from(event, isLiked);
    }

    //행사 좋아요
    @Transactional(readOnly = false)
    public SuggestedEventResponseDTO.LikeResponseDTO toggleLike(Long eventId, Long memberId) {
        SuggestedEvent event = suggestedEventRepository.findById(eventId)
                .orElseThrow(() -> new SuggestedEventException("해당 행사를 찾을 수 없습니다."));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException("사용자를 찾을 수 없습니다."));

        // 이 행사에 이미 좋아요를 눌렀는지 확인
        Optional<EventLike> eventLike = eventLikeRepository.findByMemberAndSuggestedEvent(member, event);

        boolean isLiked;

        // 이미 좋아요를 누른 경우(토글 형식으로)
        if (eventLike.isPresent()) {
            eventLikeRepository.delete(eventLike.get());
            event.decrementLikeCount();
            isLiked = false;
        } else {
            EventLike newLike = EventLike.builder()
                    .member(member)
                    .suggestedEvent(event)
                    .build();
            eventLikeRepository.save(newLike); // 새로운 '좋아요' 기록 저장
            event.incrementLikeCount(); // 행사 엔티티의 좋아요 카운트 증가
            isLiked = true;
        }

        return SuggestedEventResponseDTO.LikeResponseDTO.builder()
                .isLiked(isLiked)
                .likeCount(event.getLikeCount())
                .build();
    }

    //행사 랭킹 조회(좋아요 많은 순서대로 5개)
    public List<SuggestedEventResponseDTO> getTopSuggestedEvents(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("사용자를 찾을 수 없습니다."));

        // 좋아요 순으로 상위 5개 행사 조회
        List<SuggestedEvent> top5Events = suggestedEventRepository.findTop5ByOrderByLikeCountDesc();

        // 조회된 5개 행사에 대한 '좋아요' 상태를 계산하여 DTO로 변환 (쿼리 1번)
        Set<Long> likedEventIds = getLikedEventIds(top5Events, member);

        return top5Events.stream()
                .map(event -> SuggestedEventResponseDTO.from(event, likedEventIds))
                .toList();
    }

    //전체 행사 랭킹 조회(페이지네이션)
    public Page<SuggestedEventResponseDTO> getAllSuggestedEvents(Long memberId, Pageable pageable) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("사용자를 찾을 수 없습니다."));

        // 모든 행사를 페이징하여 조회
        Page<SuggestedEvent> eventPage = suggestedEventRepository.findAll(pageable);
        Set<Long> likedEventIds = getLikedEventIds(eventPage.getContent(), member);

        // 조회된 페이지의 행사에 대한 '좋아요' 상태를 계산하여 DTO 페이지로 변환
        return eventPage.map(event -> SuggestedEventResponseDTO.from(event, likedEventIds));
    }

    //내가 쓴 행사 조회(페이지네이션)
    public Page<SuggestedEventResponseDTO> getMySuggestedEvents(Long memberId, Pageable pageable) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("사용자를 찾을 수 없습니다."));

        // 현재 사용자가 제안한 행사만 페이징하여 조회
        Page<SuggestedEvent> myEventPage = suggestedEventRepository.findByProposer(member, pageable);
        Set<Long> likedEventIds = getLikedEventIds(myEventPage.getContent(), member);

        // 조회된 페이지의 행사에 대한 '좋아요' 상태를 계산하여 DTO 페이지로 변환
        return myEventPage.map(event -> SuggestedEventResponseDTO.from(event, likedEventIds));
    }

    private Set<Long> getLikedEventIds(List<SuggestedEvent> events, Member member) {
        if (events.isEmpty()) {
            return Collections.emptySet();
        }
        List<Long> eventIds = events.stream().map(SuggestedEvent::getId).toList();
        return eventLikeRepository.findLikedEventIdsByMemberAndEventIds(member, eventIds);
    }
}
