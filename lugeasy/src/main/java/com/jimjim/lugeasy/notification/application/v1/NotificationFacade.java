package com.jimjim.lugeasy.notification.application.v1;

import com.jimjim.lugeasy.notification.application.v1.dto.NotificationResponseDTO;

public interface NotificationFacade {
    
    /**
     * 회원의 알림 목록을 조회합니다 (오늘/지난 알림 분리).
     * 
     * @param memberId 회원 ID
     * @return 알림 목록 (오늘/지난 알림 분리)
     */
    NotificationResponseDTO.NotificationListResponse getNotifications(Long memberId);
    
    /**
     * 특정 알림을 삭제합니다.
     * 
     * @param notificationId 알림 ID
     * @param memberId 회원 ID
     */
    void deleteNotification(Long notificationId, Long memberId);
    
    /**
     * 회원의 모든 알림을 삭제합니다.
     * 
     * @param memberId 회원 ID
     */
    void deleteAllNotifications(Long memberId);
    
    /**
     * 회원의 오늘 알림을 모두 삭제합니다.
     * 
     * @param memberId 회원 ID
     */
    void deleteTodayNotifications(Long memberId);
    
    /**
     * 회원의 지난 알림을 모두 삭제합니다.
     * 
     * @param memberId 회원 ID
     */
    void deleteEarlierNotifications(Long memberId);
}