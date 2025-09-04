package com.ComNCheck.ComNCheck.domain.member.service;

import com.ComNCheck.ComNCheck.domain.global.exception.MemberException;
import com.ComNCheck.ComNCheck.domain.member.model.dto.SocialProfileDTO;
import com.ComNCheck.ComNCheck.domain.member.model.entity.Member;
import com.ComNCheck.ComNCheck.domain.member.model.entity.Role;
import com.ComNCheck.ComNCheck.domain.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional
@RequiredArgsConstructor
public class CommonMemberService {

    private final MemberRepository memberRepository;

    private static final String ADMIN_EMAIL_1 = "comncheck0306@gmail.com";
    private static final String ADMIN_EMAIL_2 = "another0306@gmail.com";

    /**
     * 앱/웹 로직에서 공통으로 사용하는 메서드
     */
    public Member getOrRegister(SocialProfileDTO profile) {

        // 1. 접근 허용 검증 로직
        if (!isAllowedUser(profile.getEmail(), profile.getHd())) {
            throw new MemberException("허용되지 않은 호스팅 도메인 혹은 계정입니다.");
        }

        // 2. 회원 조회 또는 신규 가입 로직
        return memberRepository.findByEmail(profile.getEmail()).orElseGet(() -> {
            String name = extractName(profile.getName());
            String major = extractMajor(profile.getName());

            Member newMember = Member.builder()
                    .email(profile.getEmail())
                    .name(name)
                    .major(major)
                    .role(Role.ROLE_STUDENT)
                    .studentNumber(123456789)
                    .build();
            return memberRepository.save(newMember);
        });
    }

    private boolean isAllowedUser(String email, String hd) {
        return "hufs.ac.kr".equals(hd)
                || ADMIN_EMAIL_1.equals(email)
                || ADMIN_EMAIL_2.equals(email);
    }

    private String cleanString(String input) {
        if (input == null) {
            return null;
        }
        return input.replaceAll("\\p{C}", "");
    }
    private String extractMajor(String name) {
        if (name == null) {
            return null;
        }

        String cleanedName = cleanString(name);
        Pattern pattern = Pattern.compile("/([^\\]]+)]");
        Matcher matcher = pattern.matcher(cleanedName);

        if (matcher.find()) {
            return matcher.group(1).trim();
        }

        return null;
    }

    private String extractName(String name) {
        if (name == null) {
            return null;
        }

        String cleanedName = cleanString(name);
        Pattern pattern = Pattern.compile("^([^\\[]+)");
        Matcher matcher = pattern.matcher(cleanedName);

        if (matcher.find()) {
            return matcher.group(1).trim();
        }

        return null;
    }
}