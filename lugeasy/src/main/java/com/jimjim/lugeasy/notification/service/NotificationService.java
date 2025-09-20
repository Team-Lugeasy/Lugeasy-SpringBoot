package com.jimjim.lugeasy.notification.service;

import com.jimjim.lugeasy.match.domain.Matching;
import com.jimjim.lugeasy.notification.domain.Notification;
import com.jimjim.lugeasy.user.domain.Member;

import java.util.List;

public interface NotificationService {
    
    /**
     * 매칭 상태 변경에 따른 알림을 생성합니다.
     * 
     * @param matching 매칭 정보
     * @param targetMember 알림을 받을 회원
     * @return 생성된 알림
     */
    Notification createNotification(Matching matching, Member targetMember);
    
    /**
     * 알림을 조회합니다.
     * 
     * @param notificationId 알림 ID
     * @param memberId 회원 ID
     * @return 알림 정보
     */
    Notification getNotification(Long notificationId, Long memberId);
    
    /**
     * 알림을 삭제합니다.
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
    
    /**
     * 회원의 오늘 알림 목록을 조회합니다.
     * 
     * @param memberId 회원 ID
     * @return 오늘 알림 목록
     */
    List<Notification> getTodayNotifications(Long memberId);
    
    /**
     * 회원의 지난 알림 목록을 조회합니다.
     * 
     * @param memberId 회원 ID
     * @return 지난 알림 목록
     */
    List<Notification> getEarlierNotifications(Long memberId);
}