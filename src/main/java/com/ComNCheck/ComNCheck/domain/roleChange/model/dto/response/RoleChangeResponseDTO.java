package com.ComNCheck.ComNCheck.domain.roleChange.model.dto.response;

import com.ComNCheck.ComNCheck.domain.roleChange.model.entity.RequestStatus;
import com.ComNCheck.ComNCheck.domain.roleChange.model.entity.RoleChange;
import com.ComNCheck.ComNCheck.domain.member.model.entity.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RoleChangeResponseDTO {

    private Long requestId;
    private Long memberId;
    private String name;
    private String major;
    private int studentNumber;
    private String position;
    private Role requestedRole;
    private RequestStatus status;

    public static RoleChangeResponseDTO of(RoleChange entity) {
        return RoleChangeResponseDTO.builder()
                .requestId(entity.getRequestId())
                .memberId(entity.getMember().getMemberId())
                .name(entity.getMember().getName())
                .major(entity.getMember().getMajor())
                .studentNumber(entity.getMember().getStudentNumber())
                .position(entity.getRequestPosition())
                .requestedRole(entity.getRequestRole())
                .status(entity.getStatus())
                .build();
    }
}