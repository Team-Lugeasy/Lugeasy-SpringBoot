package com.jimjim.lugeasy.review.infrastructure;

import com.jimjim.lugeasy.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    
    // 특정 호스트의 리뷰들 조회
    @Query("SELECT r FROM Review r JOIN r.matching m WHERE m.host.id = :hostId")
    List<Review> findByHostId(@Param("hostId") Long hostId);
    
    // 특정 호스트의 평균 평점 조회
    @Query("SELECT AVG(r.rating) FROM Review r JOIN r.matching m WHERE m.host.id = :hostId")
    Double getAverageRatingByHostId(@Param("hostId") Long hostId);
    
    // 특정 호스트의 리뷰 개수 조회
    @Query("SELECT COUNT(r) FROM Review r JOIN r.matching m WHERE m.host.id = :hostId")
    Long getReviewCountByHostId(@Param("hostId") Long hostId);
}
