package com.jimjim.lugeasy.notification.infrastructure;

import com.jimjim.lugeasy.notification.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    
    /**
     * 회원의 알림 목록을 조회합니다.
     * 
     * @param memberId 회원 ID
     * @return 알림 목록
     */
    List<Notification> findByMemberIdOrderByCreatedAtDesc(Long memberId);
    
    /**
     * 회원의 모든 알림을 조회합니다 (디버깅용).
     * 
     * @param memberId 회원 ID
     * @return 알림 목록
     */
    List<Notification> findByMemberId(Long memberId);
    
    /**
     * 회원의 오늘 알림 목록을 조회합니다.
     * 
     * @param memberId 회원 ID
     * @param startOfDay 오늘 시작 시간
     * @param endOfDay 오늘 끝 시간
     * @return 오늘 알림 목록
     */
    @Query("SELECT n FROM Notification n WHERE n.member.id = :memberId " +
           "AND n.createdAt >= :startOfDay AND n.createdAt < :endOfDay " +
           "ORDER BY n.createdAt DESC")
    List<Notification> findTodayNotificationsByMemberId(
            @Param("memberId") Long memberId,
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay
    );
    
    /**
     * 회원의 지난 알림 목록을 조회합니다.
     * 
     * @param memberId 회원 ID
     * @param startOfDay 오늘 시작 시간
     * @return 지난 알림 목록
     */
    @Query("SELECT n FROM Notification n WHERE n.member.id = :memberId " +
           "AND n.createdAt < :startOfDay " +
           "ORDER BY n.createdAt DESC")
    List<Notification> findEarlierNotificationsByMemberId(
            @Param("memberId") Long memberId,
            @Param("startOfDay") LocalDateTime startOfDay
    );
    
    /**
     * 회원의 특정 알림을 조회합니다.
     * 
     * @param notificationId 알림 ID
     * @param memberId 회원 ID
     * @return 알림
     */
    @Query("SELECT n FROM Notification n WHERE n.id = :notificationId AND n.member.id = :memberId")
    Notification findByIdAndMemberId(@Param("notificationId") Long notificationId, @Param("memberId") Long memberId);
    
    /**
     * 회원의 모든 알림을 삭제합니다.
     * 
     * @param memberId 회원 ID
     */
    void deleteByMemberId(Long memberId);
    
    /**
     * 회원의 오늘 알림을 모두 삭제합니다.
     * 
     * @param memberId 회원 ID
     * @param startOfDay 오늘 시작 시간
     * @param endOfDay 오늘 끝 시간
     */
    @Query("DELETE FROM Notification n WHERE n.member.id = :memberId " +
           "AND n.createdAt >= :startOfDay AND n.createdAt < :endOfDay")
    void deleteTodayNotificationsByMemberId(
            @Param("memberId") Long memberId,
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay
    );
    
    /**
     * 회원의 지난 알림을 모두 삭제합니다.
     * 
     * @param memberId 회원 ID
     * @param startOfDay 오늘 시작 시간
     */
    @Query("DELETE FROM Notification n WHERE n.member.id = :memberId " +
           "AND n.createdAt < :startOfDay")
    void deleteEarlierNotificationsByMemberId(
            @Param("memberId") Long memberId,
            @Param("startOfDay") LocalDateTime startOfDay
    );
}