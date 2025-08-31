package com.jimjim.lugeasy.review.service;

import com.jimjim.lugeasy.review.domain.Review;

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
}
