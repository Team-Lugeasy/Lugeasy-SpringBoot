package com.jimjim.lugeasy.review.service;

import com.jimjim.lugeasy.review.application.v1.dto.ReviewRequestDTO;
import com.jimjim.lugeasy.review.domain.Review;
import com.jimjim.lugeasy.user.domain.Member;

import java.util.List;

public interface ReviewService {
    
    /**
     * 특정 호스트의 리뷰 목록을 조회합니다. (단일 도메인)
     * 
     * @param hostId 호스트 ID
     * @return 리뷰 목록
     */
    List<Review> getReviewsByHostId(Long hostId);
    
    /**
     * 특정 호스트의 평균 평점을 조회합니다. (단일 도메인)
     * 
     * @param hostId 호스트 ID
     * @return 평균 평점
     */
    Double getAverageRatingByHostId(Long hostId);
    
    /**
     * 특정 호스트의 리뷰 개수를 조회합니다. (단일 도메인)
     * 
     * @param hostId 호스트 ID
     * @return 리뷰 개수
     */
    Long getReviewCountByHostId(Long hostId);
    
    /**
     * 리뷰를 생성합니다.
     */
    ReviewRequestDTO.CreateReviewResponse createReview(
            Member member, 
            Long matchId, 
            String content, 
            Integer rating
    );
    
    /**
     * 리뷰를 수정합니다.
     */
    ReviewRequestDTO.UpdateReviewResponse updateReview(
            Long reviewId, 
            Long memberId, 
            String content, 
            Integer rating
    );
    
    /**
     * 리뷰를 삭제합니다.
     */
    void deleteReview(Long reviewId, Long memberId);
    
    /**
     * 회원의 리뷰 목록을 조회합니다.
     */
    List<ReviewRequestDTO.CreateReviewResponse> getReviewsByMemberId(Long memberId);
}
