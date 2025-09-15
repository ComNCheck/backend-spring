package com.ComNCheck.ComNCheck.domain.member.service;

import com.ComNCheck.ComNCheck.domain.global.exception.GoogleException;
import com.ComNCheck.ComNCheck.domain.member.model.dto.SocialProfileDTO;
import com.ComNCheck.ComNCheck.domain.member.model.dto.response.LoginResponseDTO;
import com.ComNCheck.ComNCheck.domain.member.model.entity.Member;
import com.ComNCheck.ComNCheck.domain.security.util.JWTUtil;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GoogleAppAuthService {
    private final CommonMemberService commonMemberService;

    private final GoogleIdTokenVerifier googleIdTokenVerifier;

    private final JWTUtil jwtUtil;

    @Transactional
    public LoginResponseDTO login(String idToken) {

        GoogleIdToken.Payload payload = verifyGoogleIdToken(idToken);

        SocialProfileDTO profile = SocialProfileDTO.builder()
                .email(payload.getEmail())
                .name((String) payload.get("name"))
                .major((String) payload.get("name"))
                .hd((String) payload.get("hd"))
                .build();

        Member member = commonMemberService.getOrRegister(profile);

        // TODO: 매직 넘버 없애고 만료 시간 설정 필요
        String accessToken = jwtUtil.createJwt(member.getMemberId(), member.getName(), member.getRole().name(), 60 * 60 * 1000L);
        String refreshToken = jwtUtil.createJwt(member.getMemberId(), member.getName(), member.getRole().name(), 14 * 24 * 60 * 60 * 1000L);

        Long now = System.currentTimeMillis();
        Long accessTokenExpiresAt = now + 60 * 60 * 1000L;
        Long refreshTokenExpiresAt = now + 14 * 24 * 60 * 60 * 1000L; // ★ Refresh Token 만료 시각 계산 추가


        // TODO: Refresh Token을 Redis에 저장하는 로직 필요
        return LoginResponseDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .accessTokenExpiresAt(accessTokenExpiresAt)
                .refreshTokenExpiresAt(refreshTokenExpiresAt)
                .build();
    }

    private GoogleIdToken.Payload verifyGoogleIdToken(String idTokenString) {
        try {
            GoogleIdToken idToken = googleIdTokenVerifier.verify(idTokenString);

            if (idToken == null) {
                throw new GoogleException("유효하지 않은 Google ID 토큰입니다.");
            } else {
                return idToken.getPayload();
            }
        } catch (Exception e) {
            throw new GoogleException("Google ID 토큰 검증 중 오류가 발생했습니다.");
        }
    }
}