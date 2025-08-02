package com.jimjim.lugeasy.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AuthenticationRequest {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AuthSignIn {
        @Schema(description = "암호화된 사용자 식별자", defaultValue = "encryptedUserId123")
        private String encryptedUserIdentifier;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AuthRefreshAccessToken {
        private String refreshToken;
    }
}
