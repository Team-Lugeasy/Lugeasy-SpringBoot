package com.jimjim.lugeasy.review.application.v1;

import com.jimjim.lugeasy.review.application.v1.dto.ReviewResponseDTO;

public interface ReviewFacade {
    
    /**
     * 호스트별 리뷰 목록을 조회합니다.
     * 
     * @param hostId 호스트 ID
     * @return 호스트 리뷰 목록 정보
     */
    ReviewResponseDTO.HostReviewList getHostReviews(Long hostId);
}
