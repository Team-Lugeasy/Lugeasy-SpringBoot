package com.jimjim.lugeasy.review.service.impl;

import com.jimjim.lugeasy.review.domain.Review;
import com.jimjim.lugeasy.review.infrastructure.ReviewRepository;
import com.jimjim.lugeasy.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    @Override
    public List<Review> getReviewsByHostId(Long hostId) {
        // 단일 도메인(Review)만 처리
        return reviewRepository.findByHostId(hostId);
    }
    
    @Override
    public Double getAverageRatingByHostId(Long hostId) {
        return reviewRepository.getAverageRatingByHostId(hostId);
    }
    
    @Override
    public Long getReviewCountByHostId(Long hostId) {
        return reviewRepository.getReviewCountByHostId(hostId);
    }
}
