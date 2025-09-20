package com.jimjim.lugeasy.notification.representation.v1;

import com.jimjim.lugeasy.common.response.BaseResponse;
import com.jimjim.lugeasy.common.security.AuthenticationMember;
import com.jimjim.lugeasy.notification.application.v1.NotificationFacade;
import com.jimjim.lugeasy.notification.application.v1.dto.NotificationResponseDTO;
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

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
@Tag(name = "Notification", description = "알림 관련 API")
public class NotificationController {
    
    private final NotificationFacade notificationFacade;
    
    @GetMapping
    @Operation(summary = "알림 목록 조회", description = "회원의 알림 목록을 오늘/지난 알림으로 분리하여 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "알림 목록 조회 성공",
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(
                    name = "알림 목록 조회 성공 응답",
                    value = """
                    {
                      "timestamp": "2024-01-01T10:00:00.000Z",
                      "code": "COMMON200",
                      "message": "요청에 성공하였습니다.",
                      "result": {
                        "todayNotifications": [
                          {
                            "notificationId": 1,
                            "message": "홍길동님과의 매칭이 완료되었어요.",
                            "matchingStatus": "MATCHED",
                            "otherMemberName": "홍길동",
                            "isRead": false,
                            "createdAt": "2024-01-01T10:00:00"
                          }
                        ],
                        "earlierNotifications": [
                          {
                            "notificationId": 2,
                            "message": "김철수님에게 매칭 요청이 들어왔어요.",
                            "matchingStatus": "REQUESTED",
                            "otherMemberName": "김철수",
                            "isRead": true,
                            "createdAt": "2024-01-01T09:00:00"
                          }
                        ]
                      }
                    }
                    """
                )
            )
        )
    })
    public BaseResponse<NotificationResponseDTO.NotificationListResponse> getNotifications(
            @Parameter(hidden = true) @AuthenticationMember Member member) {
        
        return BaseResponse.onSuccess(notificationFacade.getNotifications(member.getId()));
    }
    
    @DeleteMapping("/{notificationId}")
    @Operation(summary = "알림 삭제", description = "특정 알림을 삭제합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "알림 삭제 성공"),
        @ApiResponse(responseCode = "404", description = "알림을 찾을 수 없음")
    })
    public BaseResponse<Void> deleteNotification(
            @Parameter(description = "알림 ID", example = "1") @PathVariable Long notificationId,
            @Parameter(hidden = true) @AuthenticationMember Member member) {
        
        notificationFacade.deleteNotification(notificationId, member.getId());
        return BaseResponse.onSuccess(null);
    }
    
    @DeleteMapping
    @Operation(summary = "모든 알림 삭제", description = "회원의 모든 알림을 삭제합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "모든 알림 삭제 성공")
    })
    public BaseResponse<Void> deleteAllNotifications(
            @Parameter(hidden = true) @AuthenticationMember Member member) {
        
        notificationFacade.deleteAllNotifications(member.getId());
        return BaseResponse.onSuccess(null);
    }
    
    @DeleteMapping("/today")
    @Operation(summary = "오늘 알림 삭제", description = "회원의 오늘 알림을 모두 삭제합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "오늘 알림 삭제 성공")
    })
    public BaseResponse<Void> deleteTodayNotifications(
            @Parameter(hidden = true) @AuthenticationMember Member member) {
        
        notificationFacade.deleteTodayNotifications(member.getId());
        return BaseResponse.onSuccess(null);
    }
    
    @DeleteMapping("/earlier")
    @Operation(summary = "지난 알림 삭제", description = "회원의 지난 알림을 모두 삭제합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "지난 알림 삭제 성공")
    })
    public BaseResponse<Void> deleteEarlierNotifications(
            @Parameter(hidden = true) @AuthenticationMember Member member) {
        
        notificationFacade.deleteEarlierNotifications(member.getId());
        return BaseResponse.onSuccess(null);
    }
}