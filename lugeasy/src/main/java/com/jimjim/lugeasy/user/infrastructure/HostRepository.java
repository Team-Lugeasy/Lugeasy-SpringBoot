package com.jimjim.lugeasy.user.infrastructure;

import com.jimjim.lugeasy.user.domain.Host;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
}
