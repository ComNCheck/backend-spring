package com.ComNCheck.ComNCheck.domain.member.service;

import com.ComNCheck.ComNCheck.domain.global.exception.MemberException;
import com.ComNCheck.ComNCheck.domain.member.model.dto.SocialProfileDTO;
import com.ComNCheck.ComNCheck.domain.security.oauth.CustomOAuth2Member;
import com.ComNCheck.ComNCheck.domain.member.model.dto.response.MemberDTO;
import com.ComNCheck.ComNCheck.domain.member.model.entity.Member;
import com.ComNCheck.ComNCheck.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomOAuthMemberService extends DefaultOAuth2UserService {
    private final MemberRepository memberRepository;
    private final CommonMemberService commonMemberService;

    private final static String ADMIN_EMAIL_1 = "comncheck0306@gmail.com";
    private final static String ADMIN_EMAIL_2 = "another0306@gmail.com";

    @Value("${domain.url}")
    String url;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

//        String email = oAuth2User.getAttribute("email");
//        String name = extractName(oAuth2User.getAttribute("name"));
//        String major = extractMajor(oAuth2User.getAttribute("name"));
//        //String sub = oAuth2User.getAttribute("sub"); 이메일 변경 여부 따지고 변경될경우 findByEmail 대신 findBySub 사용
//        String hd = oAuth2User.getAttribute("hd");

        SocialProfileDTO profile = SocialProfileDTO.builder()
                .email(oAuth2User.getAttribute("email"))
                .name(oAuth2User.getAttribute("name"))
                .major(oAuth2User.getAttribute("name"))
                .hd(oAuth2User.getAttribute("hd"))
                .build();

        try {
            // ★ SocialMemberService 호출
            Member member = commonMemberService.getOrRegister(profile);
            return new CustomOAuth2Member(MemberDTO.of(member));

        } catch (MemberException e) {
            // MemberException을 잡아서 OAuth2AuthenticationException으로 변환
            OAuth2Error oauth2Error = new OAuth2Error(
                    "invalid_hosted_domain",
                    e.getMessage(), // 공통 서비스의 에러 메시지를 그대로 사용
                    url + "/login?error=invalid_domain"
            );
            throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString());
        }
    }
}

