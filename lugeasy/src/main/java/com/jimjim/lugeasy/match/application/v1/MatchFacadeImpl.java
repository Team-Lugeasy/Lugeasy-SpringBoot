package com.jimjim.lugeasy.match.application.v1;

import com.jimjim.lugeasy.match.application.v1.dto.MatchRequestDTO;
import com.jimjim.lugeasy.match.domain.Matching;
import com.jimjim.lugeasy.match.domain.MatchingStatus;
import com.jimjim.lugeasy.match.service.MatchService;
import com.jimjim.lugeasy.notification.service.NotificationService;
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
    
    // Notification Domain (알림 생성용)
    private final NotificationService notificationService;

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
        MatchRequestDTO.CreateMatchResponse response = matchService.createMatch(member, hostId, dropOffTime, findingTime);
        
        // 매칭 생성 후 호스트에게 알림 전송
        Matching createdMatching = matchService.getMatchingById(response.getMatchId());
        Member hostMember = createdMatching.getHost().getMember();
        notificationService.createNotification(createdMatching, hostMember);
        
        return response;
    }

    @Override
    public List<MatchRequestDTO.MatchDetail> getMatchesByMemberId(Long memberId) {
        // MatchService를 통해 매칭 목록 조회 (Match 도메인)
        return matchService.getMatchesByMemberId(memberId);
    }
    
    @Override
    public List<MatchRequestDTO.MatchDetail> getMatchesByUserId(Long userId) {
        // MatchService를 통해 통합 매칭 목록 조회 (Match 도메인)
        return matchService.getMatchesByUserId(userId);
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
    
    @Override
    @Transactional
    public MatchRequestDTO.MatchDetail updateMatchingStatus(Long matchId, String newStatus, Long memberId) {
        // 매칭 상태 변경 (Match 도메인)
        MatchingStatus status = MatchingStatus.valueOf(newStatus);
        Matching updatedMatching = matchService.updateMatchingStatus(matchId, status, memberId);
        
        // 알림 생성 (Notification 도메인)
        // 상태를 변경한 사람이 아닌 상대방에게 알림 전송
        Member targetMember;
        if (updatedMatching.getHost().getMember().getId().equals(memberId)) {
            // 호스트가 변경했으므로 멤버에게 알림
            targetMember = updatedMatching.getMember();
        } else {
            // 멤버가 변경했으므로 호스트에게 알림
            targetMember = updatedMatching.getHost().getMember();
        }
        notificationService.createNotification(updatedMatching, targetMember);
        
        // DTO 변환하여 반환
        return MatchRequestDTO.MatchDetail.builder()
                .matchId(updatedMatching.getId())
                .hostId(updatedMatching.getHost().getId())
                .hostName(updatedMatching.getHost().getMember().getName())
                .hostAddress(updatedMatching.getHost().getAddress())
                .hostProfileImg(updatedMatching.getHost().getMember().getProfileImageUrl())
                .memberId(updatedMatching.getMember().getId())
                .memberName(updatedMatching.getMember().getName())
                .dropOffTime(updatedMatching.getDropOffTime())
                .findingTime(updatedMatching.getFindingTime())
                .status(updatedMatching.getMatchingStatus().toString())
                .userRole(updatedMatching.getHost().getMember().getId().equals(memberId) ? "HOST" : "MEMBER")
                .createdAt(updatedMatching.getCreatedAt())
                .build();
    }
}
