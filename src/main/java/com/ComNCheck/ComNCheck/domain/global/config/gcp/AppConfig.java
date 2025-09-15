package com.ComNCheck.ComNCheck.domain.global.config.gcp;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class AppConfig {
    private final GoogleOauthProperties googleOauthProperties;

    @Bean
    public GoogleIdTokenVerifier googleIdTokenVerifier() {
        List<String> clientIds = googleOauthProperties.getClientIds();

        return new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(clientIds)
                .build();
    }
}