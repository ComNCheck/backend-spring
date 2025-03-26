package com.ComNCheck.ComNCheck.domain.member.repository;

import com.ComNCheck.ComNCheck.domain.member.model.entity.Member;
import com.ComNCheck.ComNCheck.domain.member.model.entity.Role;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
    Optional<Member> findByMemberId(Long memberId);
    //Optional<Member> findBySub(String email);

    Optional<Member> findByRole(Role role);

    List<Member> findAllByRole(Role role);
    List<Member> findByAlarmMajorNoticeTrue();
    List<Member> findByAlarmEmploymentNoticeTrue();
    List<Member> findByAlarmMajorEventTrue();

}
