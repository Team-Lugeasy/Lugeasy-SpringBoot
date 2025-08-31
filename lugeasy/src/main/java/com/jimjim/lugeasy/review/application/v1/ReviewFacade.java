package com.jimjim.lugeasy.review.application.v1;

import com.jimjim.lugeasy.review.application.v1.dto.ReviewRequestDTO;
import com.jimjim.lugeasy.review.application.v1.dto.ReviewResponseDTO;

import java.util.List;

public interface ReviewFacade {
    
    /**
     * 호스트별 리뷰 목록을 조회합니다.
     * 
     * @param hostId 호스트 ID
     * @return 호스트 리뷰 목록 정보
     */
    ReviewResponseDTO.HostReviewList getHostReviews(Long hostId);
    
    /**
     * 리뷰를 생성합니다.
     */
    ReviewRequestDTO.CreateReviewResponse createReview(
            Long memberId, 
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
