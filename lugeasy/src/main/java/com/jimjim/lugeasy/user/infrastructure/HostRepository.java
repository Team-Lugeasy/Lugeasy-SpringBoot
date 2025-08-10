package com.jimjim.lugeasy.user.infrastructure;

import com.jimjim.lugeasy.user.domain.Host;
import com.jimjim.lugeasy.user.domain.HostTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface HostRepository extends JpaRepository<Host, Long> {
    
    // 인증된 호스트만 조회
    List<Host> findByIsAuthenticationTrue();
    
    // 호스트와 멤버 정보를 함께 조회 (N+1 문제 방지)
    @Query("SELECT h FROM Host h JOIN FETCH h.member WHERE h.isAuthentication = true")
    List<Host> findAuthenticatedHostsWithMember();
    
    // 특정 호스트 상세정보 조회
    @Query("SELECT h FROM Host h JOIN FETCH h.member WHERE h.id = :hostId")
    Optional<Host> findByIdWithMember(@Param("hostId") Long hostId);
    
    // 특정 호스트의 특정 기간 내 예약 가능 시간 조회
    @Query("SELECT ht FROM HostTime ht WHERE ht.host.id = :hostId AND ht.availableTime BETWEEN :startDate AND :endDate ORDER BY ht.availableTime ASC")
    List<HostTime> findAvailableTimesByHostIdAndDateRange(
            @Param("hostId") Long hostId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
}
