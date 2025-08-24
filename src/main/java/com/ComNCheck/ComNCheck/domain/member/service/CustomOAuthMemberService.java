package com.ComNCheck.ComNCheck.domain.member.service;
import com.ComNCheck.ComNCheck.domain.security.oauth.CustomOAuth2Member;
import com.ComNCheck.ComNCheck.domain.member.model.dto.response.MemberDTO;
import com.ComNCheck.ComNCheck.domain.member.model.entity.Member;
import com.ComNCheck.ComNCheck.domain.member.model.entity.Role;
import com.ComNCheck.ComNCheck.domain.member.repository.MemberRepository;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
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

    private final static String ADMIN_EMAIL_1 = "comncheck0306@gmail.com";
    private final static String ADMIN_EMAIL_2 = "another0306@gmail.com";

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String email = oAuth2User.getAttribute("email");
        String name = extractName(oAuth2User.getAttribute("name"));
        String major = extractMajor(oAuth2User.getAttribute("name"));
        //String sub = oAuth2User.getAttribute("sub"); 이메일 변경 여부 따지고 변경될경우 findByEmail 대신 findBySub 사용
        String hd = oAuth2User.getAttribute("hd");

        if (!isAllowedUser(email, hd)) {
            OAuth2Error oauth2Error = new OAuth2Error(
                    "invalid_hosted_domain",
                    "허용되지 않은 호스팅 도메인 혹은 계정입니다.",
                    "http://r-cube.iptime.org:3000/login?error=invalid_domain"
            );
            throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString());
        }

        // 이메일 변경 가능시 sub 변수
        Member member = memberRepository.findByEmail(email).orElseGet(() -> {
            Member newMember = Member.builder()
                    .email(email)
                    .name(name)
                    .major(major)
                    .role(Role.ROLE_STUDENT)
                    .studentNumber(123456789)
                    .build();
            memberRepository.save(newMember);
            return newMember;
            });

            return new CustomOAuth2Member(MemberDTO.of(member));
    }

    private boolean isAllowedUser(String email, String hd) {
        if ("hufs.ac.kr".equals(hd)
                || ADMIN_EMAIL_1.equals(email)
                || ADMIN_EMAIL_2.equals(email)) {
            return true;
        }
        return false;
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

