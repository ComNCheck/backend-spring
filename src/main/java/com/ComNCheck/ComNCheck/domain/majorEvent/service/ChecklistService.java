package com.ComNCheck.ComNCheck.domain.majorEvent.service;

import com.ComNCheck.ComNCheck.domain.global.config.validator.MemberValidator;
import com.ComNCheck.ComNCheck.domain.global.exception.EventChecklistException;
import com.ComNCheck.ComNCheck.domain.majorEvent.model.dto.converter.EventConverter;
import com.ComNCheck.ComNCheck.domain.majorEvent.model.dto.response.ChecklistResponseDTO;
import com.ComNCheck.ComNCheck.domain.majorEvent.model.entity.EventChecklist;
import com.ComNCheck.ComNCheck.domain.majorEvent.model.entity.EventChecklistItem;
import com.ComNCheck.ComNCheck.domain.majorEvent.repository.EventChecklistItemRepository;
import com.ComNCheck.ComNCheck.domain.majorEvent.repository.EventChecklistRepository;
import com.ComNCheck.ComNCheck.domain.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class ChecklistService {
    private final EventChecklistRepository eventChecklistRepository;
    private final EventChecklistItemRepository eventChecklistItemRepository; // Item 레포지토리 주입
    private final MemberValidator memberValidator;

    // 월별 체크리스트 조회
    public ChecklistResponseDTO.MonthlyChecklistDTO getMonthlyChecklist(int startMonth, int endMonth, Long memberId) {
        memberValidator.findMemberAndCheckRole(memberId);

        List<EventChecklist> checklists = eventChecklistRepository.findByMonthRange(startMonth, endMonth);

        List<ChecklistResponseDTO.EventChecklistDTO> checklistDTOs = EventConverter.toEventChecklists(checklists);

        return EventConverter.toMonthlyChecklistResponse(startMonth, endMonth, checklistDTOs);
    }

    //체크 상태 변경
    @Transactional
    public ChecklistResponseDTO.ItemDTO updateChecklistItemStatus(Long itemId, boolean isChecked, Long memberId) {
        memberValidator.findMemberAndCheckRole(memberId);

        EventChecklistItem item = eventChecklistItemRepository.findById(itemId)
                .orElseThrow(() -> new EventChecklistException("체크리스트 아이템을 찾을 수 없습니다. ID: " + itemId));

        if (isChecked) {
            item.check();
        } else {
            item.uncheck();
        }

        eventChecklistItemRepository.save(item);

        return ChecklistResponseDTO.ItemDTO.builder()
                .id(item.getId())
                .content(item.getContent())
                .isChecked(item.isChecked())
                .build();
    }
}
