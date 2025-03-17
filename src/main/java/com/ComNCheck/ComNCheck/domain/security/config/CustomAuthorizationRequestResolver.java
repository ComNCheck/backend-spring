package com.ComNCheck.ComNCheck.domain.security.config;

import jakarta.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

public class CustomAuthorizationRequestResolver implements OAuth2AuthorizationRequestResolver {

    private final OAuth2AuthorizationRequestResolver defaultResolver;

    public CustomAuthorizationRequestResolver(OAuth2AuthorizationRequestResolver defaultResolver) {
        this.defaultResolver = defaultResolver;
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
        OAuth2AuthorizationRequest oAuth2AuthorizationRequest = this.defaultResolver.resolve(request);
        return customizeRequest(oAuth2AuthorizationRequest);
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request, String clientRegistrationId) {
        OAuth2AuthorizationRequest oAuth2AuthorizationRequest = this.defaultResolver.resolve(request, clientRegistrationId);
        return customizeRequest(oAuth2AuthorizationRequest);
    }

    private OAuth2AuthorizationRequest customizeRequest(OAuth2AuthorizationRequest oAuth2AuthorizationRequest) {
        if (oAuth2AuthorizationRequest == null) {
            return null;
        }

        Map<String, Object> extraParameters = new LinkedHashMap<>(oAuth2AuthorizationRequest.getAdditionalParameters());
        extraParameters.put("prompt", "select_account");

        return OAuth2AuthorizationRequest.from(oAuth2AuthorizationRequest)
                .additionalParameters(extraParameters)
                .build();
    }
}
