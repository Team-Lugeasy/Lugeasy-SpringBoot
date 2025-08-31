package com.jimjim.lugeasy.review.infrastructure;

import com.jimjim.lugeasy.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    
    // 특정 호스트의 리뷰들 조회 (리뷰어 정보 포함)
    @Query("SELECT r FROM Review r JOIN FETCH r.matching m JOIN FETCH m.member WHERE m.host.id = :hostId ORDER BY r.createdAt DESC")
    List<Review> findByHostId(@Param("hostId") Long hostId);
    
    // 특정 호스트의 평균 평점 조회
    @Query("SELECT AVG(r.rating) FROM Review r JOIN r.matching m WHERE m.host.id = :hostId")
    Double getAverageRatingByHostId(@Param("hostId") Long hostId);
    
    // 특정 호스트의 리뷰 개수 조회
    @Query("SELECT COUNT(r) FROM Review r JOIN r.matching m WHERE m.host.id = :hostId")
    Long getReviewCountByHostId(@Param("hostId") Long hostId);
    
    /**
     * 매칭 ID로 리뷰 존재 여부를 확인합니다.
     */
    boolean existsByMatchingId(Long matchingId);
    
    /**
     * 회원 ID로 리뷰 목록을 조회합니다.
     */
    @Query("SELECT r FROM Review r JOIN FETCH r.matching m JOIN FETCH m.member WHERE m.member.id = :memberId ORDER BY r.createdAt DESC")
    List<Review> findByMemberId(@Param("memberId") Long memberId);
}
