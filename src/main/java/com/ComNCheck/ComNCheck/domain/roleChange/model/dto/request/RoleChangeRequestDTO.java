package com.ComNCheck.ComNCheck.domain.roleChange.model.dto.request;

import com.ComNCheck.ComNCheck.domain.member.model.entity.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleChangeRequestDTO {
    private String name;
    private int studentNumber;
    private String major;
    private String requestPosition;
    private Role  requestRole;
}
