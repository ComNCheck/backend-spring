package com.ComNCheck.ComNCheck.domain.roleChange.model.entity;


import com.ComNCheck.ComNCheck.domain.member.model.entity.Member;
import com.ComNCheck.ComNCheck.domain.member.model.entity.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;



@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class RoleChange {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private Long requestId;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    @Column(name = "request_position", nullable = false)
    private String requestPosition;

    @Column(name = "request_role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role requestRole;

    @Builder
    public RoleChange(Member member, String requestPosition, Role requestRole) {
        this.member = member;
        this.status = RequestStatus.PENDING;
        this.requestPosition = requestPosition;
        this.requestRole = requestRole;
    }

    public void approve() {
        this.status = RequestStatus.APPROVED;
    }


}
