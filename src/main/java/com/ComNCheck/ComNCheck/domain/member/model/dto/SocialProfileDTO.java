package com.ComNCheck.ComNCheck.domain.member.model.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SocialProfileDTO {
    private String email;
    private String name; // 구글에서 받은 name (예: "홍길동 [컴퓨터공학부]")
    private String major;
    private String hd; // 호스팅 도메인 (예: "hufs.ac.kr")
}