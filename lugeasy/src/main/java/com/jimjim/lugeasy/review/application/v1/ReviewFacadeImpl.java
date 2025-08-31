package com.jimjim.lugeasy.review.application.v1;

import com.jimjim.lugeasy.review.application.v1.dto.ReviewRequestDTO;
import com.jimjim.lugeasy.review.application.v1.dto.ReviewResponseDTO;
import com.jimjim.lugeasy.review.domain.Review;
import com.jimjim.lugeasy.review.service.ReviewService;
import com.jimjim.lugeasy.user.domain.Host;
import com.jimjim.lugeasy.user.domain.Member;
import com.jimjim.lugeasy.user.service.HostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewFacadeImpl implements ReviewFacade {

    // Review Domain
    private final ReviewService reviewService;

    // User Domain (호스트 정보 조회용)
    private final HostService hostService;

    /**
     * 호스트별 리뷰 목록을 조회합니다.
     * 단일 도메인(Review)과 다른 도메인(User)의 정보를 조합하여 반환합니다.
     * 
     * @param hostId 호스트 ID
     * @return 호스트 리뷰 목록 정보
     */
    @Override
    public ReviewResponseDTO.HostReviewList getHostReviews(Long hostId) {
        // 호스트 정보 조회 (User 도메인)
        Host host = hostService.getHostById(hostId);
        
        // ReviewService를 통해 리뷰 관련 데이터 조회 (Review 도메인)
        List<Review> reviews = reviewService.getReviewsByHostId(hostId);
        Double averageRating = reviewService.getAverageRatingByHostId(hostId);
        Long reviewCount = reviewService.getReviewCountByHostId(hostId);
        
        // 리뷰 상세 정보 변환
        List<ReviewResponseDTO.ReviewDetail> reviewDetails = reviews.stream()
                .map(this::convertToReviewDetail)
                .collect(Collectors.toList());
        
        return ReviewResponseDTO.HostReviewList.builder()
                .hostId(host.getId())
                .hostName(host.getMember().getName())
                .averageRating(averageRating != null ? averageRating : 0.0)
                .reviewCount(reviewCount != null ? reviewCount : 0L)
                .reviews(reviewDetails)
                .build();
    }
    
    @Override
    @Transactional
    public ReviewRequestDTO.CreateReviewResponse createReview(
            Long memberId, 
            Long matchId, 
            String content, 
            Integer rating) {
        
        // 회원 정보 조회 (User 도메인)
        Member member = hostService.getMemberById(memberId);
        
        // ReviewService를 통해 리뷰 생성 (Review 도메인)
        return reviewService.createReview(member, matchId, content, rating);
    }
    
    @Override
    @Transactional
    public ReviewRequestDTO.UpdateReviewResponse updateReview(
            Long reviewId, 
            Long memberId, 
            String content, 
            Integer rating) {
        
        // ReviewService를 통해 리뷰 수정 (Review 도메인)
        return reviewService.updateReview(reviewId, memberId, content, rating);
    }
    
    @Override
    @Transactional
    public void deleteReview(Long reviewId, Long memberId) {
        
        // ReviewService를 통해 리뷰 삭제 (Review 도메인)
        reviewService.deleteReview(reviewId, memberId);
    }
    
    @Override
    public List<ReviewRequestDTO.CreateReviewResponse> getReviewsByMemberId(Long memberId) {
        
        // ReviewService를 통해 회원의 리뷰 목록 조회 (Review 도메인)
        return reviewService.getReviewsByMemberId(memberId);
    }
    
    /**
     * 리뷰 엔티티를 DTO로 변환합니다.
     * 
     * @param review 리뷰 엔티티
     * @return 리뷰 상세 DTO
     */
    private ReviewResponseDTO.ReviewDetail convertToReviewDetail(Review review) {
        return ReviewResponseDTO.ReviewDetail.builder()
                .reviewId(review.getId())
                .content(review.getContent())
                .rating(review.getRating())
                .reviewerName(review.getMatching().getMember().getName())
                .createdAt(review.getCreatedAt())
                .build();
    }
}
