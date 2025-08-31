package com.jimjim.lugeasy.review.application.v1.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class ReviewResponseDTO {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "호스트 리뷰 목록 응답 DTO")
    public static class HostReviewList {
        @Schema(description = "호스트 ID")
        private Long hostId;
        
        @Schema(description = "호스트 이름")
        private String hostName;
        
        @Schema(description = "평균 평점")
        private Double averageRating;
        
        @Schema(description = "리뷰 개수")
        private Long reviewCount;
        
        @Schema(description = "리뷰 목록")
        private List<ReviewDetail> reviews;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "리뷰 상세 정보 DTO")
    public static class ReviewDetail {
        @Schema(description = "리뷰 ID")
        private Long reviewId;
        
        @Schema(description = "리뷰 내용")
        private String content;
        
        @Schema(description = "평점")
        private Integer rating;
        
        @Schema(description = "리뷰 작성자 이름")
        private String reviewerName;
        
        @Schema(description = "리뷰 작성 시간")
        private LocalDateTime createdAt;
    }
}
