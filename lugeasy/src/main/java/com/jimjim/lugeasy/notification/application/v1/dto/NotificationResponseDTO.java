package com.jimjim.lugeasy.notification.application.v1.dto;

import com.jimjim.lugeasy.match.domain.MatchingStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "알림 응답 DTO")
public class NotificationResponseDTO {
    
    @Schema(description = "알림 목록 조회 응답")
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NotificationListResponse {
        @Schema(description = "오늘 알림 목록")
        private List<NotificationDetail> todayNotifications;
        
        @Schema(description = "지난 알림 목록")
        private List<NotificationDetail> earlierNotifications;
    }
    
    @Schema(description = "알림 상세 정보")
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NotificationDetail {
        @Schema(description = "알림 ID", example = "1")
        private Long notificationId;
        
        @Schema(description = "알림 메시지", example = "홍길동님과의 매칭이 완료되었어요.")
        private String message;
        
        @Schema(description = "매칭 상태", example = "MATCHED")
        private MatchingStatus matchingStatus;
        
        @Schema(description = "상대방 이름", example = "홍길동")
        private String otherMemberName;
        
        @Schema(description = "읽음 여부", example = "false")
        private Boolean isRead;
        
        @Schema(description = "생성 시간", example = "2024-01-01T10:00:00")
        private LocalDateTime createdAt;
    }
}
