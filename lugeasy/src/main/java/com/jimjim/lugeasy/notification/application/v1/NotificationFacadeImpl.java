package com.jimjim.lugeasy.notification.application.v1;

import com.jimjim.lugeasy.match.domain.MatchingStatus;
import com.jimjim.lugeasy.notification.application.v1.dto.NotificationResponseDTO;
import com.jimjim.lugeasy.notification.domain.Notification;
import com.jimjim.lugeasy.notification.service.NotificationService;
import com.jimjim.lugeasy.notification.service.impl.NotificationServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationFacadeImpl implements NotificationFacade {
    
    // Notification Domain
    private final NotificationService notificationService;
    
    @Override
    public NotificationResponseDTO.NotificationListResponse getNotifications(Long memberId) {
        log.info("알림 조회 시작 - memberId: {}", memberId);
        
        // 디버깅용: 모든 알림 조회
        List<Notification> allNotifications = ((NotificationServiceImpl) notificationService).getAllNotificationsForDebug(memberId);
        log.info("전체 알림 개수: {}", allNotifications.size());
        allNotifications.forEach(n -> log.info("알림 ID: {}, Member ID: {}, Matching ID: {}, CreatedAt: {}", 
                n.getId(), n.getMember().getId(), n.getMatching().getId(), n.getCreatedAt()));
        
        // 오늘 알림 조회
        List<Notification> todayNotifications = notificationService.getTodayNotifications(memberId);
        log.info("오늘 알림 개수: {}", todayNotifications.size());
        
        // 지난 알림 조회
        List<Notification> earlierNotifications = notificationService.getEarlierNotifications(memberId);
        log.info("지난 알림 개수: {}", earlierNotifications.size());
        
        // DTO 변환
        List<NotificationResponseDTO.NotificationDetail> todayDetails = todayNotifications.stream()
                .map(this::convertToNotificationDetail)
                .collect(Collectors.toList());
        
        List<NotificationResponseDTO.NotificationDetail> earlierDetails = earlierNotifications.stream()
                .map(this::convertToNotificationDetail)
                .collect(Collectors.toList());
        
        return NotificationResponseDTO.NotificationListResponse.builder()
                .todayNotifications(todayDetails)
                .earlierNotifications(earlierDetails)
                .build();
    }
    
    @Override
    @Transactional
    public void deleteNotification(Long notificationId, Long memberId) {
        notificationService.deleteNotification(notificationId, memberId);
    }
    
    @Override
    @Transactional
    public void deleteAllNotifications(Long memberId) {
        notificationService.deleteAllNotifications(memberId);
    }
    
    @Override
    @Transactional
    public void deleteTodayNotifications(Long memberId) {
        notificationService.deleteTodayNotifications(memberId);
    }
    
    @Override
    @Transactional
    public void deleteEarlierNotifications(Long memberId) {
        notificationService.deleteEarlierNotifications(memberId);
    }
    
    /**
     * 알림 엔티티를 DTO로 변환합니다.
     * 
     * @param notification 알림 엔티티
     * @return 알림 상세 DTO
     */
    private NotificationResponseDTO.NotificationDetail convertToNotificationDetail(Notification notification) {
        // 메시지 동적 생성
        String message = createNotificationMessage(notification);
        
        return NotificationResponseDTO.NotificationDetail.builder()
                .notificationId(notification.getId())
                .message(message)
                .matchingStatus(notification.getMatchingStatus())
                .otherMemberName(notification.getOtherMemberName())
                .isRead(notification.getIsRead())
                .createdAt(notification.getCreatedAt())
                .build();
    }
    
    /**
     * 알림 메시지를 동적으로 생성합니다.
     * 
     * @param notification 알림 정보
     * @return 생성된 메시지
     */
    private String createNotificationMessage(Notification notification) {
        String otherMemberName = notification.getOtherMemberName();
        MatchingStatus matchingStatus = notification.getMatchingStatus();
        
        return switch (matchingStatus) {
            case REQUESTED -> otherMemberName + "님에게 매칭 요청이 들어왔어요.";
            case MATCHED -> otherMemberName + "님과의 매칭이 완료되었어요.";
            case COMPLETED -> otherMemberName + "님과의 매칭이 완료되었어요.";
            case REJECTED -> otherMemberName + "님의 매칭 요청이 거절되었어요.";
        };
    }
}