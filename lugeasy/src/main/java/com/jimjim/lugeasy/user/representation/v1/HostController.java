package com.jimjim.lugeasy.user.representation.v1;

import com.jimjim.lugeasy.common.response.BaseResponse;
import com.jimjim.lugeasy.user.application.v1.dto.HostListResponseDTO;
import com.jimjim.lugeasy.user.service.HostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
