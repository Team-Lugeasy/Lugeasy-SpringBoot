package com.jimjim.lugeasy.user.service.impl;

import com.jimjim.lugeasy.review.infrastructure.ReviewRepository;
import com.jimjim.lugeasy.user.application.v1.dto.HostListResponseDTO;
import com.jimjim.lugeasy.user.domain.Host;
import com.jimjim.lugeasy.user.infrastructure.HostRepository;
import com.jimjim.lugeasy.user.service.HostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HostServiceImpl implements HostService {

    private final HostRepository hostRepository;
    private final ReviewRepository reviewRepository;

    @Override
    public List<HostListResponseDTO> getAuthenticatedHostList() {
        // 인증된 호스트들을 멤버 정보와 함께 조회
        List<Host> authenticatedHosts = hostRepository.findAuthenticatedHostsWithMember();
        
        return authenticatedHosts.stream()
                .map(this::convertToHostListResponseDTO)
                .collect(Collectors.toList());
    }
    
    private HostListResponseDTO convertToHostListResponseDTO(Host host) {
        // 호스트의 평균 평점 조회
        Double averageRating = reviewRepository.getAverageRatingByHostId(host.getId());
        
        // 호스트의 리뷰 개수 조회
        Long reviewCount = reviewRepository.getReviewCountByHostId(host.getId());
        
        return HostListResponseDTO.builder()
                .name(host.getMember().getName())
                .averageRating(averageRating != null ? averageRating : 0.0)
                .reviewCount(reviewCount != null ? reviewCount : 0L)
                .address(host.getAddress())
                .build();
    }
}
