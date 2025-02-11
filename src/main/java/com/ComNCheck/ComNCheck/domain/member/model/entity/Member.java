package com.ComNCheck.ComNCheck.domain.member.model.entity;

import com.ComNCheck.ComNCheck.domain.fcm.model.entity.FcmToken;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "name", nullable = false)
    private String name;

     // 실제 배포에서는 nullable 설정 false 해야함
    @Column(name = "major", nullable = true)
    private String major;

    @Column(name = "student_number")
    @Setter
    private int studentNumber;

    @Column(name = "member_role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "position")
    private String position;

    @Column(name = "check_student_card", nullable = false)
    private boolean checkStudentCard;

    @Column(name = "alarm_major_event", nullable = false)
    private boolean alarmMajorEvent;

    @Column(name = "alarm_major_notice", nullable = false)
    private boolean alarmMajorNotice;

    @Column(name = "alarm_employment_notice")
    private boolean alarmEmploymentNotice;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FcmToken> fcmTokens = new ArrayList<>();

    @Builder
    public Member(Long memberId, String email, String name, String major, int studentNumber, Role role) {
        this.memberId = memberId;
        this.email = email;
        this.name = name;
        this.major = major;
        this.studentNumber = studentNumber;
        this.position = null;
        this.role = role;
        this.checkStudentCard = false;
        this.alarmMajorNotice = false;
        this.alarmMajorEvent = false;
        this.alarmEmploymentNotice = false;
    }

    public void updatePosition(String requestPosition) {
        this.position = requestPosition;
    }
    public void updateRole(Role newRole) {
        this.role = newRole;
    }
    public void addFcmToken(FcmToken token) {
        this.fcmTokens.add(token);
        token.setMember(this);
    }
    public void changeCheckStudentCard() {
        this.checkStudentCard = true;
    }
    public void onAlarmMajorEvent() {
        this.alarmMajorEvent = true;
    }
    public void offAlarmMajorEvent() {
        this.alarmMajorEvent = false;
    }
    public void onAlarmMajorNotice() {
        this.alarmMajorNotice = true;
    }
    public void offAlarmMajorNotice() {
        this.alarmMajorNotice = false;
    }
    public void onAlarmEmploymentNotice() {
        this.alarmEmploymentNotice = true;
    }
    public void offAlarmEmploymentNotice() {
        this.alarmEmploymentNotice = false;
    }

}

