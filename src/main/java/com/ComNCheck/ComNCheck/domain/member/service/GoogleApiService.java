package com.ComNCheck.ComNCheck.domain.member.service;

import com.ComNCheck.ComNCheck.domain.global.exception.MemberException;
import com.ComNCheck.ComNCheck.domain.member.model.dto.SocialProfileDTO;
import com.ComNCheck.ComNCheck.domain.member.model.dto.response.LoginResponseDTO;
import com.ComNCheck.ComNCheck.domain.member.model.entity.Member;
import com.ComNCheck.ComNCheck.domain.security.util.JWTUtil;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class GoogleApiService {
    private final ClientRegistrationRepository clientRegistrationRepository;

    private final CommonMemberService commonMemberService;

    private final JWTUtil jwtUtil;
    private final WebClient webClient;
    private final RestTemplate restTemplate;

    public LoginResponseDTO login(String authorizationCode) {

        // %2F -> /로 다시 디코딩
        String decodedCode = URLDecoder.decode(authorizationCode, StandardCharsets.UTF_8);

        // 구글 클라이언트 등록 정보 가져오기
        ClientRegistration registration = clientRegistrationRepository.findByRegistrationId("google");

        // 1. 인가 코드로 구글에 액세스 토큰 요청
        String googleAccessToken = getGoogleAccessToken(decodedCode, registration);

        // 2. 액세스 토큰으로 구글에 사용자 정보 요청
        JsonNode userInfo = getGoogleUserInfo(googleAccessToken);

        // 3. 사용자 정보를 바탕으로 우리 서비스에 회원가입 또는 로그인 처리
        SocialProfileDTO profile = SocialProfileDTO.builder()
                .email(userInfo.path("email").asText())
                .name(userInfo.path("name").asText())
                .major(userInfo.path("name").asText())
                .hd(userInfo.path("hd").asText())
                .build();

        Member member = commonMemberService.getOrRegister(profile);

        // 4. 우리 서비스의 JWT 생성

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

    private String getGoogleAccessToken(String authorizationCode, ClientRegistration registration) {

        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();

        formData.add("code", authorizationCode);
        formData.add("client_id", registration.getClientId());
        formData.add("client_secret", registration.getClientSecret());
        formData.add("redirect_uri", registration.getRedirectUri());
        formData.add("grant_type", "authorization_code");

        JsonNode responseNode = webClient.post()
                .uri(registration.getProviderDetails().getTokenUri())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue(formData)
                .retrieve()

                .onStatus(HttpStatusCode::is4xxClientError, response ->
                        response.bodyToMono(String.class).flatMap(errorBody ->
                                Mono.error(new MemberException("Google 액세스 토큰 요청 실패: " + errorBody))))
                .bodyToMono(JsonNode.class)
                .block();

        return responseNode.path("access_token").asText();
    }

    private JsonNode getGoogleUserInfo(String accessToken) {
        ClientRegistration googleRegistration = clientRegistrationRepository.findByRegistrationId("google");
        String googleUserInfoUri = googleRegistration.getProviderDetails().getUserInfoEndpoint().getUri();

        return webClient.get()
                .uri(googleUserInfoUri)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();
    }
}