package com.ComNCheck.ComNCheck.domain.global.config.validator;

import com.ComNCheck.ComNCheck.domain.global.exception.ForbiddenException;
import com.ComNCheck.ComNCheck.domain.global.exception.MemberNotFoundException;
import com.ComNCheck.ComNCheck.domain.member.model.entity.Member;
import com.ComNCheck.ComNCheck.domain.member.model.entity.Role;
import com.ComNCheck.ComNCheck.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component // 이 클래스를 스프링 빈으로 등록
@RequiredArgsConstructor
public class MemberValidator {
    private final MemberRepository memberRepository;

    public Member findMemberAndCheckRole(Long memberId) {
        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new MemberNotFoundException("등록된 회원이 없습니다."));

        isCheckRole(member);

        return member;
    }

    public void isCheckRole(Member member) {
        Role checkRole = member.getRole();
        if(checkRole != Role.ROLE_ADMIN && checkRole != Role.ROLE_MAJOR_PRESIDENT && checkRole != Role.ROLE_STUDENT_COUNCIL) {
            throw new ForbiddenException("접근 권한이 없습니다.");
        }
    }
}
