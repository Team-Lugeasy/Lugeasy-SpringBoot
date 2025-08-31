package com.jimjim.lugeasy.match.infrastructure;

import com.jimjim.lugeasy.match.domain.Matching;
import com.jimjim.lugeasy.match.domain.MatchingStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MatchingRepository extends JpaRepository<Matching, Long> {
    
    /**
     * 회원 ID로 매칭 목록을 조회합니다.
     */
    List<Matching> findByMemberId(Long memberId);
    
    /**
     * 호스트 ID로 매칭 목록을 조회합니다.
     */
    List<Matching> findByHostId(Long hostId);
    
    /**
     * 예약 ID와 회원 ID로 매칭을 조회합니다.
     */
    Optional<Matching> findByIdAndMemberId(Long id, Long memberId);
    
    /**
     * 특정 호스트의 특정 시간대에 예약이 있는지 확인합니다.
     */
    @Query("SELECT COUNT(m) > 0 FROM Matching m WHERE m.host.id = :hostId " +
           "AND m.matchingStatus = :matchingStatus " +
           "AND ((m.dropOffTime BETWEEN :startTime AND :endTime) " +
           "OR (m.findingTime BETWEEN :startTime AND :endTime))")
    boolean existsByHostIdAndTimeRange(
            @Param("hostId") Long hostId,
            @Param("matchingStatus") MatchingStatus matchingStatus,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );
}
