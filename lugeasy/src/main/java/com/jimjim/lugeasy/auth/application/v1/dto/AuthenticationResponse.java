package com.jimjim.lugeasy.auth.application.v1.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AuthenticationResponse {
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AuthSignIn {
        private Long memberId;
        private String accessToken;
        private String refreshToken;
        private Boolean isMembered;
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