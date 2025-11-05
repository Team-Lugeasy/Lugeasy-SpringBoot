package com.jimjim.lugeasy.match.service.impl;

import com.jimjim.lugeasy.match.application.v1.dto.MatchRequestDTO;
import com.jimjim.lugeasy.match.domain.Matching;
import com.jimjim.lugeasy.match.domain.MatchingStatus;
import com.jimjim.lugeasy.match.errorCode.MatchErrorCode;
import com.jimjim.lugeasy.match.infrastructure.MatchingRepository;
import com.jimjim.lugeasy.match.service.MatchService;
import com.jimjim.lugeasy.user.domain.Host;
import com.jimjim.lugeasy.user.domain.Member;
import com.jimjim.lugeasy.user.infrastructure.HostRepository;
import com.jimjim.lugeasy.common.error.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MatchServiceImpl implements MatchService {

    private final MatchingRepository matchingRepository;
    private final HostRepository hostRepository;

    @Override
    @Transactional
    public MatchRequestDTO.CreateMatchResponse createMatch(
            Member member, 
            Long hostId, 
            LocalDateTime dropOffTime, 
            LocalDateTime findingTime) {
        
        // 호스트 존재 여부 확인
        Host host = hostRepository.findById(hostId)
                .orElseThrow(() -> new RestApiException(MatchErrorCode.HOST_NOT_FOUND));
        
        // 매칭 가능 시간 확인 (간단한 검증)
        if (dropOffTime.isAfter(findingTime)) {
            throw new RestApiException(MatchErrorCode.INVALID_TIME_RANGE);
        }
        
        // 새로운 매칭 생성
        Matching matching = Matching.builder()
                .host(host)
                .member(member)
                .matchingStatus(MatchingStatus.REQUESTED) // 요청 상태로 시작
                .dropOffTime(dropOffTime)
                .findingTime(findingTime)
                .build();
        
        Matching savedMatching = matchingRepository.save(matching);
        
        return MatchRequestDTO.CreateMatchResponse.builder()
                .matchId(savedMatching.getId())
                .status("REQUESTED")
                .message("매칭 요청이 완료되었습니다. 매칭이 완료되면 알려드릴게요.")
                .build();
    }

    @Override
    public List<MatchRequestDTO.MatchDetail> getMatchesByMemberId(Long memberId) {
        List<Matching> matchings = matchingRepository.findByMemberId(memberId);
        
        return matchings.stream()
                .map(matching -> MatchRequestDTO.MatchDetail.builder()
                        .matchId(matching.getId())
                        .hostId(matching.getHost().getId())
                        .hostName(matching.getHost().getMember().getName())
                        .hostAddress(matching.getHost().getAddress())
                        .hostProfileImg(matching.getHost().getMember().getProfileImageUrl())
                        .memberId(matching.getMember().getId())
                        .memberName(matching.getMember().getName())
                        .dropOffTime(matching.getDropOffTime())
                        .findingTime(matching.getFindingTime())
                        .status(matching.getMatchingStatus().toString())
                        .userRole("MEMBER")
                        .createdAt(matching.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }
    
    @Override
    public List<MatchRequestDTO.MatchDetail> getMatchesByUserId(Long userId) {
        // 호스트로 참여한 매칭 조회
        List<Matching> hostMatchings = matchingRepository.findByHostId(userId);
        List<MatchRequestDTO.MatchDetail> hostMatches = hostMatchings.stream()
                .map(matching -> MatchRequestDTO.MatchDetail.builder()
                        .matchId(matching.getId())
                        .hostId(matching.getHost().getId())
                        .hostName(matching.getHost().getMember().getName())
                        .hostAddress(matching.getHost().getAddress())
                        .hostProfileImg(matching.getHost().getMember().getProfileImageUrl())
                        .memberId(matching.getMember().getId())
                        .memberName(matching.getMember().getName())
                        .dropOffTime(matching.getDropOffTime())
                        .findingTime(matching.getFindingTime())
                        .status(matching.getMatchingStatus().toString())
                        .userRole("HOST")
                        .createdAt(matching.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
        
        // 회원으로 참여한 매칭 조회
        List<Matching> memberMatchings = matchingRepository.findByMemberId(userId);
        List<MatchRequestDTO.MatchDetail> memberMatches = memberMatchings.stream()
                .map(matching -> MatchRequestDTO.MatchDetail.builder()
                        .matchId(matching.getId())
                        .hostId(matching.getHost().getId())
                        .hostName(matching.getHost().getMember().getName())
                        .hostAddress(matching.getHost().getAddress())
                        .hostProfileImg(matching.getHost().getMember().getProfileImageUrl())
                        .memberId(matching.getMember().getId())
                        .memberName(matching.getMember().getName())
                        .dropOffTime(matching.getDropOffTime())
                        .findingTime(matching.getFindingTime())
                        .status(matching.getMatchingStatus().toString())
                        .userRole("MEMBER")
                        .createdAt(matching.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
        
        // 두 리스트를 합치고 생성 시간 순으로 정렬
        List<MatchRequestDTO.MatchDetail> allMatches = new ArrayList<>();
        allMatches.addAll(hostMatches);
        allMatches.addAll(memberMatches);
        
        return allMatches.stream()
                .sorted(Comparator.comparing(MatchRequestDTO.MatchDetail::getCreatedAt, 
                        Comparator.nullsLast(Comparator.reverseOrder())))
                .collect(Collectors.toList());
    }

    @Override
    public MatchRequestDTO.MatchDetail getMatchDetail(Long matchId) {
        Matching matching = matchingRepository.findById(matchId)
                .orElseThrow(() -> new RestApiException(MatchErrorCode.MATCH_NOT_FOUND));
        
        return MatchRequestDTO.MatchDetail.builder()
                .matchId(matching.getId())
                .hostId(matching.getHost().getId())
                .hostName(matching.getHost().getMember().getName())
                .hostAddress(matching.getHost().getAddress())
                .hostProfileImg(matching.getHost().getMember().getProfileImageUrl())
                .memberId(matching.getMember().getId())
                .memberName(matching.getMember().getName())
                .dropOffTime(matching.getDropOffTime())
                .findingTime(matching.getFindingTime())
                .status(matching.getMatchingStatus().toString())
                .createdAt(matching.getCreatedAt())
                .build();
    }

    @Override
    @Transactional
    public void cancelMatch(Long matchId, Long memberId) {
        // 매칭 존재 여부 확인
        Matching matching = matchingRepository.findById(matchId)
                .orElseThrow(() -> new RestApiException(MatchErrorCode.MATCH_NOT_FOUND));
        
        // 본인의 매칭인지 확인
        if (!matching.getMember().getId().equals(memberId)) {
            throw new RestApiException(MatchErrorCode.MATCH_NOT_OWNED);
        }
        
        // 매칭 취소 가능한 상태인지 확인
        if (matching.getMatchingStatus() != MatchingStatus.REQUESTED) {
            throw new RestApiException(MatchErrorCode.MATCH_CANNOT_CANCEL);
        }
        
        matchingRepository.delete(matching);
    }
    
    @Override
    public Matching getMatchingById(Long matchId) {
        return matchingRepository.findById(matchId)
                .orElseThrow(() -> new RestApiException(MatchErrorCode.MATCH_NOT_FOUND));
    }
    
    @Override
    @Transactional
    public Matching updateMatchingStatus(Long matchId, MatchingStatus newStatus, Long memberId) {
        // 매칭 존재 여부 확인
        Matching matching = matchingRepository.findById(matchId)
                .orElseThrow(() -> new RestApiException(MatchErrorCode.MATCH_NOT_FOUND));
        
        // 권한 확인 (호스트 또는 멤버만 변경 가능)
        boolean isHost = matching.getHost().getMember().getId().equals(memberId);
        boolean isMember = matching.getMember().getId().equals(memberId);
        
        if (!isHost && !isMember) {
            throw new RestApiException(MatchErrorCode.MATCH_NOT_OWNED);
        }
        
        // 상태 전환 검증
        validateStatusTransition(matching.getMatchingStatus(), newStatus, matching);
        
        // 매칭 상태 변경
        matching.updateMatchingStatus(newStatus);
        
        return matchingRepository.save(matching);
    }
    
    @Override
    public void validateStatusTransition(MatchingStatus currentStatus, MatchingStatus newStatus, Matching matching) {
        // 1. 기본 상태 전환 규칙 검증
        if (!isValidTransition(currentStatus, newStatus)) {
            throw new RestApiException(MatchErrorCode.INVALID_STATUS_TRANSITION);
        }
        
        // 2. 최종 상태에서 변경 불가 검증
        if (isFinalStatus(currentStatus)) {
            throw new RestApiException(MatchErrorCode.CANNOT_MODIFY_FINAL_STATUS);
        }
        
        // 3. 시간 기반 제약 검증
        validateTimeBasedConstraints(currentStatus, newStatus, matching);
    }
    
    /**
     * 유효한 상태 전환인지 확인합니다.
     */
    private boolean isValidTransition(MatchingStatus current, MatchingStatus next) {
        return switch (current) {
            case REQUESTED -> next == MatchingStatus.MATCHED || next == MatchingStatus.REJECTED;
            case MATCHED -> next == MatchingStatus.COMPLETED || next == MatchingStatus.REJECTED;
            case COMPLETED, REJECTED -> false; // 최종 상태
        };
    }
    
    /**
     * 최종 상태인지 확인합니다.
     */
    private boolean isFinalStatus(MatchingStatus status) {
        return status == MatchingStatus.COMPLETED || status == MatchingStatus.REJECTED;
    }
    
    /**
     * 시간 기반 제약을 검증합니다.
     */
    private void validateTimeBasedConstraints(MatchingStatus currentStatus, MatchingStatus newStatus, Matching matching) {
        LocalDateTime now = LocalDateTime.now();
        
        // MATCHED → REJECTED: 서비스 시작 전에만 가능
        if (currentStatus == MatchingStatus.MATCHED && newStatus == MatchingStatus.REJECTED) {
            if (now.isAfter(matching.getDropOffTime())) {
                throw new RestApiException(MatchErrorCode.CANNOT_CANCEL_AFTER_SERVICE_START);
            }
        }
        
        // MATCHED → COMPLETED: 서비스 종료 후에만 가능
        if (currentStatus == MatchingStatus.MATCHED && newStatus == MatchingStatus.COMPLETED) {
            if (now.isBefore(matching.getFindingTime())) {
                throw new RestApiException(MatchErrorCode.CANNOT_COMPLETE_BEFORE_SERVICE_END);
            }
        }
    }
}
