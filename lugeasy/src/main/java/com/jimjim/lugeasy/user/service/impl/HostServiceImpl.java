package com.jimjim.lugeasy.user.service.impl;

import com.jimjim.lugeasy.review.infrastructure.ReviewRepository;
import com.jimjim.lugeasy.user.application.v1.dto.HostListResponseDTO;
import com.jimjim.lugeasy.user.application.v1.dto.HostDetailResponseDTO;
import com.jimjim.lugeasy.user.application.v1.dto.HostAvailabilityResponseDTO;
import com.jimjim.lugeasy.user.domain.Host;
import com.jimjim.lugeasy.user.domain.HostTime;
import com.jimjim.lugeasy.user.infrastructure.HostRepository;
import com.jimjim.lugeasy.user.service.HostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
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
                .id(host.getId())
                .name(host.getMember().getName())
                .averageRating(averageRating != null ? averageRating : 0.0)
                .reviewCount(reviewCount != null ? reviewCount : 0L)
                .address(host.getAddress())
                .build();
    }
    
    @Override
    public HostDetailResponseDTO getHostDetail(Long hostId) {
        // 호스트와 멤버 정보를 함께 조회
        Host host = hostRepository.findByIdWithMember(hostId)
                .orElseThrow(() -> new RuntimeException("호스트를 찾을 수 없습니다. ID: " + hostId));
        
        // 호스트의 평균 평점 조회
        Double averageRating = reviewRepository.getAverageRatingByHostId(hostId);
        
        // 호스트의 리뷰 개수 조회
        Long reviewCount = reviewRepository.getReviewCountByHostId(hostId);
        
        return HostDetailResponseDTO.builder()
                .id(host.getId())
                .name(host.getMember().getName())
                .introduce(host.getIntroduce())
                .averageRating(averageRating != null ? averageRating : 0.0)
                .reviewCount(reviewCount != null ? reviewCount : 0L)
                .address(host.getAddress())
                .build();
    }
    
    @Override
    public HostAvailabilityResponseDTO getHostAvailability(Long hostId, LocalDate startDate, LocalDate endDate) {
        // 호스트 존재 여부 확인
        hostRepository.findById(hostId)
                .orElseThrow(() -> new RuntimeException("호스트를 찾을 수 없습니다. ID: " + hostId));

        // 시작 날짜와 종료 날짜의 시간 범위 설정
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX); // 해당 날짜의 마지막 시간까지 포함

        // 특정 기간 내 예약 가능한 시간 조회
        List<HostTime> availableTimes = hostRepository.findAvailableTimesByHostIdAndDateRange(hostId, startDateTime, endDateTime);

        // 날짜별로 그룹화하고, 각 날짜의 시간대를 1-24 정수형으로 변환
        Map<String, List<Integer>> availabilityMap = availableTimes.stream()
                .collect(Collectors.groupingBy(
                        hostTime -> hostTime.getAvailableTime().toLocalDate().format(DateTimeFormatter.ISO_DATE), // YYYY-MM-DD
                        Collectors.mapping(
                                hostTime -> hostTime.getAvailableTime().getHour() + 1, // 0-23시를 1-24로 매핑
                                Collectors.toCollection(TreeSet::new) // 중복 제거 및 자동 정렬을 위해 TreeSet 사용
                        )
                ))
                .entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().stream().sorted().collect(Collectors.toList()) // 정렬된 리스트로 변환
                ));

        return HostAvailabilityResponseDTO.builder()
                .availability(availabilityMap)
                .build();
    }
    
    @Override
    public Host getHostById(Long hostId) {
        return hostRepository.findByIdWithMember(hostId)
                .orElseThrow(() -> new RuntimeException("호스트를 찾을 수 없습니다. ID: " + hostId));
    }
}
