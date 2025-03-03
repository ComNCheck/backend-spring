package com.ComNCheck.ComNCheck.domain.member.model.dto.response;

import com.ComNCheck.ComNCheck.domain.member.model.entity.Member;

import com.ComNCheck.ComNCheck.domain.member.model.entity.Role;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MemberInformationResponseDTO {

    private Long memberId;
    private String name;
    private String major;
    private int studentNumber;
    private Role role;
    private boolean checkStudentCard;
    private boolean alarmMajorEvent;
    private boolean alarmMajorNotice;
    private boolean alarmEmploymentNotice;
    public static MemberInformationResponseDTO of(Member member) {
        return MemberInformationResponseDTO.builder()
                .memberId(member.getMemberId())
                .name(member.getName())
                .major(member.getMajor())
                .studentNumber(member.getStudentNumber())
                .role(member.getRole())
                .checkStudentCard(member.isCheckStudentCard())
                .alarmMajorEvent(member.isAlarmMajorEvent())
                .alarmMajorNotice(member.isAlarmMajorNotice())
                .alarmEmploymentNotice(member.isAlarmEmploymentNotice())
                .build();
    }
}
