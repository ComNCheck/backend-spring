package com.ComNCheck.ComNCheck.domain.roleChange.model.dto.response;


import com.ComNCheck.ComNCheck.domain.roleChange.model.entity.RequestStatus;
import com.ComNCheck.ComNCheck.domain.roleChange.model.entity.RoleChange;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RoleChangeListDTO {

    private Long requestId;
    private String name;
    private String requestPosition;
    private RequestStatus status;

    public static RoleChangeListDTO of(RoleChange roleChange) {
        return RoleChangeListDTO.builder()
                .requestId(roleChange.getRequestId())
                .name(roleChange.getMember().getName())
                .requestPosition(roleChange.getRequestPosition())
                .status(roleChange.getStatus())
                .build();
    }
}
