package com.ComNCheck.ComNCheck.domain.security.oauth;

import com.ComNCheck.ComNCheck.domain.member.model.dto.response.MemberDTO;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

@RequiredArgsConstructor
public class CustomOAuth2Member implements OAuth2User {
    private final MemberDTO memberDTO;

    public MemberDTO getMemberDTO() {
        return memberDTO;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return memberDTO.getRole().getValue();
            }
        });
        return collection;
    }

    @Override
    public String getName() {
        return memberDTO.getName();
    }

    public boolean isCheckStudentCard() {
        return memberDTO.isCheckStudentCard();
    }

}
