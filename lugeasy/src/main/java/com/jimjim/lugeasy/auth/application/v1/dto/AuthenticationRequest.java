package com.jimjim.lugeasy.auth.application.v1.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AuthenticationRequest {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "로그인 요청 DTO")
    public static class AuthSignIn {
        @Schema(description = "암호화된 사용자 식별자", example = "encrypted_user_identifier", required = true)
        @NotBlank(message = "암호화된 사용자 식별자는 필수입니다")
        private String encryptedUserIdentifier;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AuthRefreshAccessToken {
        @Schema(description = "리프레시 토큰", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
        @NotBlank(message = "리프레시 토큰은 필수입니다")
        private String refreshToken;
    }
} 