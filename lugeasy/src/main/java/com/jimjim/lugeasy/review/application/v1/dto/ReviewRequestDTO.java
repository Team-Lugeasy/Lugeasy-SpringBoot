package com.jimjim.lugeasy.review.application.v1.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ReviewRequestDTO {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "리뷰 작성 DTO")
    public static class CreateReviewRequest {
        @Schema(description = "매칭 ID", example = "1", required = true)
        @NotNull(message = "매칭 ID는 필수입니다")
        private Long matchId;

        @Schema(description = "리뷰 내용", example = "정말 좋은 서비스였습니다!", required = true)
        @NotBlank(message = "리뷰 내용은 필수입니다")
        private String content;

        @Schema(description = "평점 (1-5)", example = "5", required = true)
        @NotNull(message = "평점은 필수입니다")
        @Min(value = 1, message = "평점은 1 이상이어야 합니다")
        @Max(value = 5, message = "평점은 5 이하여야 합니다")
        private Integer rating;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "리뷰 수정 DTO")
    public static class UpdateReviewRequest {
        @Schema(description = "리뷰 내용", example = "수정된 리뷰 내용입니다!", required = true)
        @NotBlank(message = "리뷰 내용은 필수입니다")
        private String content;

        @Schema(description = "평점 (1-5)", example = "4", required = true)
        @NotNull(message = "평점은 필수입니다")
        @Min(value = 1, message = "평점은 1 이상이어야 합니다")
        @Max(value = 5, message = "평점은 5 이하여야 합니다")
        private Integer rating;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "리뷰 작성 응답 DTO")
    public static class CreateReviewResponse {
        @Schema(description = "리뷰 ID")
        private Long reviewId;

        @Schema(description = "매칭 ID")
        private Long matchId;

        @Schema(description = "리뷰 내용")
        private String content;

        @Schema(description = "평점")
        private Integer rating;

        @Schema(description = "작성자 이름")
        private String reviewerName;

        @Schema(description = "작성 시간")
        private String createdAt;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "리뷰 수정 응답 DTO")
    public static class UpdateReviewResponse {
        @Schema(description = "리뷰 ID")
        private Long reviewId;

        @Schema(description = "리뷰 내용")
        private String content;

        @Schema(description = "평점")
        private Integer rating;

        @Schema(description = "수정 시간")
        private String updatedAt;
    }
}
