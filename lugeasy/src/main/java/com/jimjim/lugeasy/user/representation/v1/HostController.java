package com.jimjim.lugeasy.user.representation.v1;

import com.jimjim.lugeasy.common.response.BaseResponse;
import com.jimjim.lugeasy.user.application.v1.dto.HostListResponseDTO;
import com.jimjim.lugeasy.user.application.v1.dto.HostDetailResponseDTO;
import com.jimjim.lugeasy.user.application.v1.dto.HostAvailabilityResponseDTO;
import com.jimjim.lugeasy.user.service.HostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "호스트 목록 조회 성공",
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(
                    name = "호스트 목록 조회 성공 응답",
                    value = """
                    {
                      "timestamp": "2025-08-31T09:45:40.426159645Z",
                      "code": "COMMON200",
                      "message": "요청에 성공하였습니다.",
                      "result": [
                        {
                          "host_id": 1,
                          "name": "호스트 이름",
                          "description": "호스트 설명"
                        }
                      ]
                    }
                    """
                )
            )
        )
    })
    @GetMapping
    public BaseResponse<List<HostListResponseDTO>> getAuthenticatedHostList() {
        List<HostListResponseDTO> hostList = hostService.getAuthenticatedHostList();
        return BaseResponse.onSuccess(hostList);
    }
    
    @Operation(summary = "호스트 상세정보 조회", description = "특정 호스트의 상세정보를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "호스트 상세정보 조회 성공",
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(
                    name = "호스트 상세정보 조회 성공 응답",
                    value = """
                    {
                      "timestamp": "2025-08-31T09:45:40.426159645Z",
                      "code": "COMMON200",
                      "message": "요청에 성공하였습니다.",
                      "result": {
                        "host_id": 1,
                        "name": "호스트 이름",
                        "description": "호스트 설명",
                        "profile_image_url": "https://example.com/image.jpg"
                      }
                    }
                    """
                )
            )
        )
    })
    @GetMapping("/{hostId}")
    public BaseResponse<HostDetailResponseDTO> getHostDetail(@PathVariable Long hostId) {
        HostDetailResponseDTO hostDetail = hostService.getHostDetail(hostId);
        return BaseResponse.onSuccess(hostDetail);
    }
    
    @Operation(summary = "호스트 예약 가능 날짜 및 시간 조회", description = "특정 호스트의 지정된 기간 내 예약 가능한 날짜와 시간대를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "호스트 가능 시간 조회 성공",
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(
                    name = "호스트 가능 시간 조회 성공 응답",
                    value = """
                    {
                      "timestamp": "2025-08-31T09:45:40.426159645Z",
                      "code": "COMMON200",
                      "message": "요청에 성공하였습니다.",
                      "result": {
                        "host_id": 1,
                        "available_dates": [
                          {
                            "date": "2024-01-01",
                            "available_times": ["09:00", "10:00", "11:00"]
                          }
                        ]
                      }
                    }
                    """
                )
            )
        )
    })
    @GetMapping("/{hostId}/availability")
    public BaseResponse<HostAvailabilityResponseDTO> getHostAvailability(
            @Parameter(description = "호스트 ID", example = "1") @PathVariable Long hostId,
            @Parameter(description = "조회 시작 날짜 (YYYY-MM-DD 형식)", example = "2024-01-01") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "조회 종료 날짜 (YYYY-MM-DD 형식)", example = "2024-12-31") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        HostAvailabilityResponseDTO availability = hostService.getHostAvailability(hostId, startDate, endDate);
        return BaseResponse.onSuccess(availability);
    }
}
