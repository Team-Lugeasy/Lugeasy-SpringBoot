package com.jimjim.lugeasy.user.representation.v1;

import com.jimjim.lugeasy.common.response.BaseResponse;
import com.jimjim.lugeasy.user.application.v1.dto.HostListResponseDTO;
import com.jimjim.lugeasy.user.application.v1.dto.HostDetailResponseDTO;
import com.jimjim.lugeasy.user.application.v1.dto.HostAvailabilityResponseDTO;
import com.jimjim.lugeasy.user.service.HostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Host", description = "호스트 관련 API")
@RestController
@RequestMapping("/api/v1/hosts")
@RequiredArgsConstructor
public class HostController {

    private final HostService hostService;

    @Operation(summary = "인증된 호스트 목록 조회", description = "인증된 호스트들의 목록을 조회합니다.")
    @GetMapping
    public BaseResponse<List<HostListResponseDTO>> getAuthenticatedHostList() {
        List<HostListResponseDTO> hostList = hostService.getAuthenticatedHostList();
        return BaseResponse.onSuccess(hostList);
    }
    
    @Operation(summary = "호스트 상세정보 조회", description = "특정 호스트의 상세정보를 조회합니다.")
    @GetMapping("/{hostId}")
    public BaseResponse<HostDetailResponseDTO> getHostDetail(@PathVariable Long hostId) {
        HostDetailResponseDTO hostDetail = hostService.getHostDetail(hostId);
        return BaseResponse.onSuccess(hostDetail);
    }
    
    @Operation(summary = "호스트 예약 가능 날짜 및 시간 조회", description = "특정 호스트의 지정된 기간 내 예약 가능한 날짜와 시간대를 조회합니다.")
    @GetMapping("/{hostId}/availability")
    public BaseResponse<HostAvailabilityResponseDTO> getHostAvailability(
            @PathVariable Long hostId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        HostAvailabilityResponseDTO availability = hostService.getHostAvailability(hostId, startDate, endDate);
        return BaseResponse.onSuccess(availability);
    }
}
