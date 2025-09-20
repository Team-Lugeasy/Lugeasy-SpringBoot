package com.jimjim.lugeasy.notification.service.impl;

import com.jimjim.lugeasy.common.error.RestApiException;
import com.jimjim.lugeasy.match.domain.Matching;
import com.jimjim.lugeasy.notification.domain.Notification;
import com.jimjim.lugeasy.notification.errorCode.NotificationErrorCode;
import com.jimjim.lugeasy.notification.infrastructure.NotificationRepository;
import com.jimjim.lugeasy.notification.service.NotificationService;
import com.jimjim.lugeasy.user.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationServiceImpl implements NotificationService {
    
    private final NotificationRepository notificationRepository;
    
    @Override
    @Transactional
    public Notification createNotification(Matching matching, Member targetMember) {
        // 알림 생성 (메시지는 동적으로 생성)
        Notification notification = Notification.builder()
                .member(targetMember)
                .matching(matching)
                .isRead(false)
                .build();
        
        return notificationRepository.save(notification);
    }
    
    @Override
    public Notification getNotification(Long notificationId, Long memberId) {
        Notification notification = notificationRepository.findByIdAndMemberId(notificationId, memberId);
        if (notification == null) {
            throw new RestApiException(NotificationErrorCode.NOTIFICATION_NOT_FOUND);
        }
        return notification;
    }
    
    @Override
    @Transactional
    public void deleteNotification(Long notificationId, Long memberId) {
        Notification notification = getNotification(notificationId, memberId);
        notificationRepository.delete(notification);
    }
    
    @Override
    @Transactional
    public void deleteAllNotifications(Long memberId) {
        notificationRepository.deleteByMemberId(memberId);
    }
    
    @Override
    @Transactional
    public void deleteTodayNotifications(Long memberId) {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);
        
        notificationRepository.deleteTodayNotificationsByMemberId(memberId, startOfDay, endOfDay);
    }
    
    @Override
    @Transactional
    public void deleteEarlierNotifications(Long memberId) {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        notificationRepository.deleteEarlierNotificationsByMemberId(memberId, startOfDay);
    }
    
    @Override
    public List<Notification> getTodayNotifications(Long memberId) {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);
        
        return notificationRepository.findTodayNotificationsByMemberId(memberId, startOfDay, endOfDay);
    }
    
    @Override
    public List<Notification> getEarlierNotifications(Long memberId) {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        return notificationRepository.findEarlierNotificationsByMemberId(memberId, startOfDay);
    }
    
    /**
     * 디버깅용: 회원의 모든 알림을 조회합니다.
     */
    public List<Notification> getAllNotificationsForDebug(Long memberId) {
        return notificationRepository.findByMemberId(memberId);
    }
    
}