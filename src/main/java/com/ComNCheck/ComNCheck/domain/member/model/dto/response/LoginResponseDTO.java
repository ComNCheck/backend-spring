package com.ComNCheck.ComNCheck.domain.member.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class LoginResponseDTO {
    private String accessToken;
    private String refreshToken;
    private Long accessTokenExpiresAt;
    private Long refreshTokenExpiresAt;
}
