package com.jimjim.lugeasy.match.representation.v1;

import com.jimjim.lugeasy.common.response.BaseResponse;
import com.jimjim.lugeasy.common.security.AuthenticationMember;
import com.jimjim.lugeasy.match.application.v1.MatchFacade;
import com.jimjim.lugeasy.match.application.v1.dto.MatchRequestDTO;
import com.jimjim.lugeasy.user.domain.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Match", description = "매칭 관련 API")
@RestController
@RequestMapping("/api/v1/matches")
@RequiredArgsConstructor
public class MatchController {

    private final MatchFacade matchFacade;

    @Operation(summary = "매칭 요청", description = "호스트에게 매칭을 요청합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "매칭 요청 성공",
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(
                    name = "매칭 요청 성공 응답",
                    value = """
                    {
                      "timestamp": "2025-08-31T09:45:40.426159645Z",
                      "code": "COMMON200",
                      "message": "요청에 성공하였습니다.",
                      "result": {
                        "match_id": 1,
                        "status": "REQUESTED",
                        "message": "매칭 요청이 완료되었습니다. 매칭이 완료되면 알려드릴게요."
                      }
                    }
                    """
                )
            )
        )
    })
    @PostMapping
    public BaseResponse<MatchRequestDTO.CreateMatchResponse> createMatch(
            @AuthenticationMember Member member,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "매칭 요청",
                required = true,
                content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                        name = "매칭 요청 예시",
                        value = """
                        {
                          "host_id": 1,
                          "drop_off_time": "2024-03-26T11:00:00",
                          "finding_time": "2024-03-28T23:00:00"
                        }
                        """
                    )
                )
            )
            @RequestBody MatchRequestDTO.CreateMatchRequest request) {
        
        MatchRequestDTO.CreateMatchResponse response = matchFacade.createMatch(
                member.getId(),
                request.getHostId(),
                request.getDropOffTime(),
                request.getFindingTime()
        );
        
        return BaseResponse.onSuccess(response);
    }

    @Operation(summary = "매칭 목록 조회", description = "회원의 매칭 목록을 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "매칭 목록 조회 성공",
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(
                    name = "매칭 목록 조회 성공 응답",
                    value = """
                    {
                      "timestamp": "2025-08-31T09:45:40.426159645Z",
                      "code": "COMMON200",
                      "message": "요청에 성공하였습니다.",
                      "result": [
                        {
                          "match_id": 1,
                          "host_id": 1,
                          "host_name": "김태린",
                          "host_address": "서울시 노원구 화랑로 123",
                          "drop_off_time": "2024-03-26T11:00:00",
                          "finding_time": "2024-03-28T23:00:00",
                          "status": "REQUESTED",
                          "created_at": "2024-03-25T10:00:00"
                        }
                      ]
                    }
                    """
                )
            )
        )
    })
    @GetMapping
    public BaseResponse<List<MatchRequestDTO.MatchDetail>> getMatches(
            @AuthenticationMember Member member) {
        
        List<MatchRequestDTO.MatchDetail> matches = 
                matchFacade.getMatchesByMemberId(member.getId());
        
        return BaseResponse.onSuccess(matches);
    }

    @Operation(summary = "매칭 상세 조회", description = "특정 매칭의 상세 정보를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "매칭 상세 조회 성공",
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(
                    name = "매칭 상세 조회 성공 응답",
                    value = """
                    {
                      "timestamp": "2025-08-31T09:45:40.426159645Z",
                      "code": "COMMON200",
                      "message": "요청에 성공하였습니다.",
                      "result": {
                        "match_id": 1,
                        "host_id": 1,
                        "host_name": "김태린",
                        "host_address": "서울시 노원구 화랑로 123",
                        "drop_off_time": "2024-03-26T11:00:00",
                        "finding_time": "2024-03-28T23:00:00",
                        "status": "REQUESTED",
                        "created_at": "2024-03-25T10:00:00"
                      }
                    }
                    """
                )
            )
        )
    })
    @GetMapping("/{matchId}")
    public BaseResponse<MatchRequestDTO.MatchDetail> getMatchDetail(
            @Parameter(description = "매칭 ID", example = "1") @PathVariable Long matchId) {
        
        MatchRequestDTO.MatchDetail match = 
                matchFacade.getMatchDetail(matchId);
        
        return BaseResponse.onSuccess(match);
    }

    @Operation(summary = "매칭 취소", description = "매칭을 취소합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "매칭 취소 성공",
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(
                    name = "매칭 취소 성공 응답",
                    value = """
                    {
                      "timestamp": "2025-08-31T09:45:40.426159645Z",
                      "code": "COMMON200",
                      "message": "요청에 성공하였습니다.",
                      "result": null
                    }
                    """
                )
            )
        )
    })
    @DeleteMapping("/{matchId}")
    public BaseResponse<Void> cancelMatch(
            @AuthenticationMember Member member,
            @Parameter(description = "매칭 ID", example = "1") @PathVariable Long matchId) {
        
        matchFacade.cancelMatch(matchId, member.getId());
        
        return BaseResponse.onSuccess(null);
    }
}
