package com.jimjim.lugeasy.match.application.v1;

import com.jimjim.lugeasy.match.application.v1.dto.MatchRequestDTO;
import com.jimjim.lugeasy.match.service.MatchService;
import com.jimjim.lugeasy.user.domain.Member;
import com.jimjim.lugeasy.user.service.HostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MatchFacadeImpl implements MatchFacade {

    // Match Domain
    private final MatchService matchService;

    // User Domain (회원 정보 조회용)
    private final HostService hostService;

    @Override
    @Transactional
    public MatchRequestDTO.CreateMatchResponse createMatch(
            Long memberId, 
            Long hostId, 
            LocalDateTime dropOffTime, 
            LocalDateTime findingTime) {
        
        // 회원 정보 조회 (User 도메인)
        Member member = hostService.getMemberById(memberId);
        
        // MatchService를 통해 매칭 생성 (Match 도메인)
        return matchService.createMatch(member, hostId, dropOffTime, findingTime);
    }

    @Override
    public List<MatchRequestDTO.MatchDetail> getMatchesByMemberId(Long memberId) {
        // MatchService를 통해 매칭 목록 조회 (Match 도메인)
        return matchService.getMatchesByMemberId(memberId);
    }

    @Override
    public MatchRequestDTO.MatchDetail getMatchDetail(Long matchId) {
        // MatchService를 통해 매칭 상세 조회 (Match 도메인)
        return matchService.getMatchDetail(matchId);
    }

    @Override
    @Transactional
    public void cancelMatch(Long matchId, Long memberId) {
        // MatchService를 통해 매칭 취소 (Match 도메인)
        matchService.cancelMatch(matchId, memberId);
    }
}
