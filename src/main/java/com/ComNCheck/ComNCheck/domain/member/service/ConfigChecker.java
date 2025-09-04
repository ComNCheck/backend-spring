package com.ComNCheck.ComNCheck.domain.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Component;

@Slf4j // 로그 출력을 위해 추가
@Component
@RequiredArgsConstructor
public class ConfigChecker implements CommandLineRunner {

    // Spring이 자동으로 생성해준 OAuth2 클라이언트 설정 정보 저장소를 주입받습니다.
    private final ClientRegistrationRepository clientRegistrationRepository;

    @Override
    public void run(String... args) throws Exception {
        log.info("===== 애플리케이션 시작 시 설정 값 확인 =====");

        // "google"이라는 이름으로 등록된 ClientRegistration 정보를 찾습니다.
        ClientRegistration googleRegistration = clientRegistrationRepository.findByRegistrationId("google");

        if (googleRegistration != null) {
            log.info("--- Google OAuth2 Client Registration Details ---");
            log.info("Registration ID: {}", googleRegistration.getRegistrationId());
            log.info("Client ID: {}", googleRegistration.getClientId());
            // [보안] Client Secret은 절대 로그에 직접 출력하면 안 됩니다.
            log.info("Client Secret: {}", googleRegistration.getClientSecret() != null ? "[PROTECTED]" : "null");
            log.info("Redirect URI: {}", googleRegistration.getRedirectUri());
            log.info("Scopes: {}", googleRegistration.getScopes());
            log.info("Authorization Grant Type: {}", googleRegistration.getAuthorizationGrantType().getValue());

            // Provider Details (Spring이 자동으로 설정해주는 URL 등)
            ClientRegistration.ProviderDetails providerDetails = googleRegistration.getProviderDetails();
            log.info("Authorization URI: {}", providerDetails.getAuthorizationUri());
            log.info("Token URI: {}", providerDetails.getTokenUri());
            log.info("User Info URI: {}", providerDetails.getUserInfoEndpoint().getUri());
            log.info("User Info Attribute Name: {}", providerDetails.getUserInfoEndpoint().getUserNameAttributeName());
            log.info("JWK Set URI: {}", providerDetails.getJwkSetUri());
            log.info("-------------------------------------------------");
        } else {
            log.warn("Google OAuth2 Client Registration 정보를 찾을 수 없습니다!");
        }
    }
}