package com.jimjim.lugeasy.match.application.v1.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class MatchRequestDTO {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "매칭 요청 DTO")
    public static class CreateMatchRequest {
        @Schema(description = "호스트 ID", example = "1", required = true)
        @NotNull(message = "호스트 ID는 필수입니다")
        private Long hostId;

        @Schema(description = "짐 맡기기 시간", example = "2024-03-26T11:00:00", required = true)
        @NotNull(message = "짐 맡기기 시간은 필수입니다")
        private LocalDateTime dropOffTime;

        @Schema(description = "짐 찾기 시간", example = "2024-03-28T23:00:00", required = true)
        @NotNull(message = "짐 찾기 시간은 필수입니다")
        private LocalDateTime findingTime;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "매칭 요청 응답 DTO")
    public static class CreateMatchResponse {
        @Schema(description = "매칭 ID")
        private Long matchId;

        @Schema(description = "매칭 상태")
        private String status;

        @Schema(description = "매칭 요청 완료 메시지")
        private String message;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "매칭 상세 정보 DTO")
    public static class MatchDetail {
        @Schema(description = "매칭 ID")
        private Long matchId;

        @Schema(description = "호스트 ID")
        private Long hostId;

        @Schema(description = "호스트 이름")
        private String hostName;

        @Schema(description = "호스트 주소")
        private String hostAddress;

        @Schema(description = "회원 ID")
        private Long memberId;

        @Schema(description = "회원 이름")
        private String memberName;

        @Schema(description = "짐 맡기기 시간")
        private LocalDateTime dropOffTime;

        @Schema(description = "짐 찾기 시간")
        private LocalDateTime findingTime;

        @Schema(description = "매칭 상태")
        private String status;

        @Schema(description = "사용자 역할 (HOST/MEMBER)")
        private String userRole;

        @Schema(description = "매칭 생성 시간")
        private LocalDateTime createdAt;
    }
    
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "매칭 상태 변경 요청 DTO")
    public static class UpdateMatchingStatusRequest {
        @Schema(description = "새로운 매칭 상태", example = "MATCHED", required = true)
        @NotNull(message = "매칭 상태는 필수입니다")
        private String status;
    }
}
