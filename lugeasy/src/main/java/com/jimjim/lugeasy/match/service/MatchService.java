package com.jimjim.lugeasy.match.service;

import com.jimjim.lugeasy.match.application.v1.dto.MatchRequestDTO;
import com.jimjim.lugeasy.match.domain.Matching;
import com.jimjim.lugeasy.user.domain.Member;

import java.util.List;

public interface MatchService {
    
    /**
     * 매칭을 생성합니다.
     * 
     * @param member 매칭을 요청하는 회원
     * @param hostId 호스트 ID
     * @param dropOffTime 짐 맡기기 시간
     * @param findingTime 짐 찾기 시간
     * @return 생성된 매칭 정보
     */
    MatchRequestDTO.CreateMatchResponse createMatch(
            Member member, 
            Long hostId, 
            java.time.LocalDateTime dropOffTime, 
            java.time.LocalDateTime findingTime
    );
    
    /**
     * 회원의 매칭 목록을 조회합니다.
     * 
     * @param memberId 회원 ID
     * @return 매칭 목록
     */
    List<MatchRequestDTO.MatchDetail> getMatchesByMemberId(Long memberId);
    
    /**
     * 사용자 ID로 매칭 목록을 조회합니다 (호스트/회원 모두 포함).
     * 
     * @param userId 사용자 ID
     * @return 매칭 목록
     */
    List<MatchRequestDTO.MatchDetail> getMatchesByUserId(Long userId);
    
    /**
     * 특정 매칭의 상세 정보를 조회합니다.
     * 
     * @param matchId 매칭 ID
     * @return 매칭 상세 정보
     */
    MatchRequestDTO.MatchDetail getMatchDetail(Long matchId);
    
    /**
     * 매칭을 취소합니다.
     * 
     * @param matchId 매칭 ID
     * @param memberId 회원 ID
     */
    void cancelMatch(Long matchId, Long memberId);
    
    /**
     * 매칭 ID로 매칭 엔티티를 조회합니다.
     * 
     * @param matchId 매칭 ID
     * @return 매칭 엔티티
     */
    Matching getMatchingById(Long matchId);
    
    /**
     * 매칭 상태를 변경합니다.
     * 
     * @param matchId 매칭 ID
     * @param newStatus 새로운 매칭 상태
     * @param memberId 회원 ID (권한 확인용)
     * @return 변경된 매칭 정보
     */
    Matching updateMatchingStatus(Long matchId, com.jimjim.lugeasy.match.domain.MatchingStatus newStatus, Long memberId);
}
