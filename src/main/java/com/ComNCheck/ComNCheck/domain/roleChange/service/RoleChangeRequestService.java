package com.ComNCheck.ComNCheck.domain.roleChange.service;


import com.ComNCheck.ComNCheck.domain.global.exception.ApplyNotFoundException;
import com.ComNCheck.ComNCheck.domain.global.exception.ForbiddenException;
import com.ComNCheck.ComNCheck.domain.global.exception.MemberNotFoundException;
import com.ComNCheck.ComNCheck.domain.member.model.entity.Role;
import com.ComNCheck.ComNCheck.domain.roleChange.model.dto.request.RoleChangeRequestDTO;
import com.ComNCheck.ComNCheck.domain.roleChange.model.dto.response.ApprovedRoleListDTO;
import com.ComNCheck.ComNCheck.domain.roleChange.model.dto.response.RoleChangeListDTO;
import com.ComNCheck.ComNCheck.domain.roleChange.model.dto.response.RoleChangeResponseDTO;
import com.ComNCheck.ComNCheck.domain.member.model.entity.Member;
import com.ComNCheck.ComNCheck.domain.roleChange.model.entity.RequestStatus;
import com.ComNCheck.ComNCheck.domain.roleChange.model.entity.RoleChange;
import com.ComNCheck.ComNCheck.domain.member.repository.MemberRepository;
import com.ComNCheck.ComNCheck.domain.roleChange.repository.RoleChangeRequestRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class RoleChangeRequestService {
    private final RoleChangeRequestRepository roleChangeRequestRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public RoleChangeResponseDTO createRoleChangeRequest(Long memberId, RoleChangeRequestDTO requestDTO) {
        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new MemberNotFoundException("등록된 회원이 없습니다."));

        RoleChange roleChange = RoleChange.builder()
                .member(member)
                .requestPosition(requestDTO.getRequestPosition())
                .requestRole(requestDTO.getRequestRole())
                .build();

        RoleChange saveRoleChange = roleChangeRequestRepository.save(roleChange);
        return RoleChangeResponseDTO.of(saveRoleChange);
    }

    public List<RoleChangeListDTO> getAllRequests(Long memberId) {
        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new MemberNotFoundException("등록된 회원이 없습니다."));
        isCheckRole(member);

        List<RoleChange> requests = roleChangeRequestRepository.findAllOrderByIdDesc();
        return requests.stream()
                .map(RoleChangeListDTO::of)
                .collect(Collectors.toList());
    }

    public RoleChangeResponseDTO getRequestDetail(Long requestId, Long memberId) {
        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new MemberNotFoundException("등록된 회원이 없습니다."));
        isCheckRole(member);

        RoleChange request = roleChangeRequestRepository.findById(requestId)
                .orElseThrow(() -> new ApplyNotFoundException("등록된 학생회 신청이 없습니다."));
        return RoleChangeResponseDTO.of(request);
    }

    @Transactional
    public void approveRequest(Long requestId, Long memberId) {
        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new MemberNotFoundException("등록된 회원이 없습니다."));
        isCheckRole(member);

        RoleChange request = roleChangeRequestRepository.findById(requestId)
                .orElseThrow(() -> new ApplyNotFoundException("등록된 학생회 신청이 없습니다."));

        request.approve();

        Member updateMember = request.getMember();
        updateMember.updateRole(request.getRequestRole());
        updateMember.updatePosition(request.getRequestPosition());

    }

    public List<ApprovedRoleListDTO> getApproveRequests(Long memberId) {
        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new MemberNotFoundException("등록된 회원이 없습니다."));
        isCheckRole(member);

        List<RoleChange> requests = roleChangeRequestRepository.findAll().stream()
                .filter(req -> req.getStatus() == RequestStatus.APPROVED)
                .collect(Collectors.toList());

        return requests.stream()
                .map(ApprovedRoleListDTO::of)
                .collect(Collectors.toList());
    }


    @Transactional
    public void deleteRequest(Long requestId, Long memberId) {
        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new MemberNotFoundException("등록된 회원이 없습니다."));
        isCheckRole(member);

        RoleChange request = roleChangeRequestRepository.findById(requestId)
                .orElseThrow(() -> new ApplyNotFoundException("등록된 학생회 신청이 없습니다."));
        roleChangeRequestRepository.delete(request);
    }

    @Transactional
    public RoleChangeResponseDTO updateRoleChange(Long requestId, Long memberId, String updatePosition, Role updateRole) {
        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new MemberNotFoundException("등록된 회원이 없습니다."));
        isCheckRole(member);

        RoleChange role = roleChangeRequestRepository.findById(requestId)
                .orElseThrow(() -> new ApplyNotFoundException("등록된 학생회 신청이 없습니다."));

        role.update(updatePosition, updateRole);
        Member updateMember = role.getMember();
        updateMember.updateRole(role.getRequestRole());
        updateMember.updatePosition(role.getRequestPosition());

        return RoleChangeResponseDTO.of(role);
    }

    public void isCheckRole(Member member) {
        Role checkRole = member.getRole();
        if (checkRole != Role.ROLE_MAJOR_PRESIDENT && checkRole != Role.ROLE_ADMIN) {
            throw new ForbiddenException("접근 권한이 없습니다.");
        }
    }

}
