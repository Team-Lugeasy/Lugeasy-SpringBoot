package com.jimjim.lugeasy.review.service.impl;

import com.jimjim.lugeasy.review.application.v1.dto.ReviewRequestDTO;
import com.jimjim.lugeasy.review.domain.Review;
import com.jimjim.lugeasy.review.errorCode.ReviewErrorCode;
import com.jimjim.lugeasy.review.infrastructure.ReviewRepository;
import com.jimjim.lugeasy.review.service.ReviewService;
import com.jimjim.lugeasy.match.domain.Matching;
import com.jimjim.lugeasy.match.domain.MatchingStatus;
import com.jimjim.lugeasy.match.infrastructure.MatchingRepository;
import com.jimjim.lugeasy.user.domain.Member;
import com.jimjim.lugeasy.common.error.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final MatchingRepository matchingRepository;

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
    
    @Override
    @Transactional
    public ReviewRequestDTO.CreateReviewResponse createReview(
            Member member, 
            Long matchId, 
            String content, 
            Integer rating) {
        
        // 매칭 존재 여부 확인
        Matching matching = matchingRepository.findById(matchId)
                .orElseThrow(() -> new RestApiException(ReviewErrorCode.MATCH_NOT_FOUND));
        
        // 본인의 매칭인지 확인
        if (!matching.getMember().getId().equals(member.getId())) {
            throw new RestApiException(ReviewErrorCode.MATCH_NOT_OWNED);
        }
        
        // 매칭이 완료되었는지 확인
        if (matching.getMatchingStatus() != MatchingStatus.COMPLETED) {
            throw new RestApiException(ReviewErrorCode.MATCH_NOT_COMPLETED);
        }
        
        // 이미 리뷰를 작성했는지 확인
        if (reviewRepository.existsByMatchingId(matchId)) {
            throw new RestApiException(ReviewErrorCode.REVIEW_ALREADY_EXISTS);
        }
        
        // 평점 유효성 검사
        if (rating < 1 || rating > 5) {
            throw new RestApiException(ReviewErrorCode.INVALID_RATING);
        }
        
        // 리뷰 생성
        Review review = Review.builder()
                .matching(matching)
                .content(content)
                .rating(rating)
                .build();
        
        Review savedReview = reviewRepository.save(review);
        
        return ReviewRequestDTO.CreateReviewResponse.builder()
                .reviewId(savedReview.getId())
                .matchId(matchId)
                .content(content)
                .rating(rating)
                .reviewerName(member.getName())
                .createdAt(savedReview.getCreatedAt().toString())
                .build();
    }
    
    @Override
    @Transactional
    public ReviewRequestDTO.UpdateReviewResponse updateReview(
            Long reviewId, 
            Long memberId, 
            String content, 
            Integer rating) {
        
        // 리뷰 존재 여부 확인
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RestApiException(ReviewErrorCode.REVIEW_NOT_FOUND));
        
        // 본인의 리뷰인지 확인
        if (!review.getMatching().getMember().getId().equals(memberId)) {
            throw new RestApiException(ReviewErrorCode.REVIEW_NOT_OWNED);
        }
        
        // 평점 유효성 검사
        if (rating < 1 || rating > 5) {
            throw new RestApiException(ReviewErrorCode.INVALID_RATING);
        }
        
        // 리뷰 수정
        review.updateReview(content, rating);
        Review updatedReview = reviewRepository.save(review);
        
        return ReviewRequestDTO.UpdateReviewResponse.builder()
                .reviewId(reviewId)
                .content(content)
                .rating(rating)
                .updatedAt(updatedReview.getUpdatedAt().toString())
                .build();
    }
    
    @Override
    @Transactional
    public void deleteReview(Long reviewId, Long memberId) {
        // 리뷰 존재 여부 확인
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RestApiException(ReviewErrorCode.REVIEW_NOT_FOUND));
        
        // 본인의 리뷰인지 확인
        if (!review.getMatching().getMember().getId().equals(memberId)) {
            throw new RestApiException(ReviewErrorCode.REVIEW_NOT_OWNED);
        }
        
        reviewRepository.delete(review);
    }
    
    @Override
    public List<ReviewRequestDTO.CreateReviewResponse> getReviewsByMemberId(Long memberId) {
        List<Review> reviews = reviewRepository.findByMemberId(memberId);
        
        return reviews.stream()
                .map(this::convertToCreateReviewResponse)
                .collect(Collectors.toList());
    }
    
    private ReviewRequestDTO.CreateReviewResponse convertToCreateReviewResponse(Review review) {
        return ReviewRequestDTO.CreateReviewResponse.builder()
                .reviewId(review.getId())
                .matchId(review.getMatching().getId())
                .content(review.getContent())
                .rating(review.getRating())
                .reviewerName(review.getMatching().getMember().getName())
                .createdAt(review.getCreatedAt().toString())
                .build();
    }
}
