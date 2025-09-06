package com.ComNCheck.ComNCheck.domain.security.auth;

import com.ComNCheck.ComNCheck.domain.member.model.dto.response.MemberDTO;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Getter
public class CustomUserDetails implements UserDetails, OAuth2User {
    private final MemberDTO memberDTO;
    private Map<String, Object> attributes; // OAuth2 사용자 정보를 담을 필드

    // JWT 인증 시 사용할 생성자
    public CustomUserDetails(MemberDTO memberDTO) {
        this.memberDTO = memberDTO;
    }

    // OAuth2 인증 시 사용할 생성자
    public CustomUserDetails(MemberDTO memberDTO, Map<String, Object> attributes) {
        this.memberDTO = memberDTO;
        this.attributes = attributes;
    }

    // MemberDTO에 직접 접근할 수 있는 편의 메소드
    public MemberDTO getMemberDTO() {
        return memberDTO;
    }

    // OAuth2User 구현
    @Override
    public Map<String, Object> getAttributes() {
        // 소셜 로그인 시 받아온 추가 속성들을 반환
        return null;
    }

    // UserDetails & OAuth2User 인터페이스 메소드 구현
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // MemberDTO의 Role 값을 바탕으로 권한을 생성합니다.
        return Collections.singleton(new SimpleGrantedAuthority(memberDTO.getRole().getValue()));
    }

    @Override
    public String getName() {
        return memberDTO.getName();
    }

    public boolean isCheckStudentCard() {
        return memberDTO.isCheckStudentCard();
    }

    /*
    * 안쓰는 메서드
    * */
    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return "";
    }
}