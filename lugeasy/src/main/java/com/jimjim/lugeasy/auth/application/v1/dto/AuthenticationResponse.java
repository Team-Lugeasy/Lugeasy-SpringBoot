package com.jimjim.lugeasy.auth.application.v1.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AuthenticationResponse {
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "로그인 응답 DTO")
    public static class AuthSignIn {
        @Schema(description = "회원 ID")
        private Long memberId;
        
        @Schema(description = "액세스 토큰")
        private String accessToken;
        
        @Schema(description = "리프레시 토큰")
        private String refreshToken;
        
        @Schema(description = "기존 회원 여부")
        private Boolean isMembered;
        
        @Schema(description = "회원 이름")
        private String name;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AuthResign {
        private Long memberId;
    }


    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AuthRenewAccessToken {
        private Long memberId;
        private String accessToken;
        private String refreshToken;
    }
} 