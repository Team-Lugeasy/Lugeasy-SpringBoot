package com.jimjim.lugeasy.notification.domain;

import com.jimjim.lugeasy.common.entity.BaseEntity;
import com.jimjim.lugeasy.match.domain.Matching;
import com.jimjim.lugeasy.match.domain.MatchingStatus;
import com.jimjim.lugeasy.user.domain.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "matching_id", nullable = false)
    private Matching matching;
    
    @Builder.Default
    @Column(nullable = false)
    private Boolean isRead = false;
    
    /**
     * 매칭 상태를 반환합니다.
     * 
     * @return 매칭 상태
     */
    public MatchingStatus getMatchingStatus() {
        return this.matching.getMatchingStatus();
    }
    
    /**
     * 상대방 이름을 반환합니다.
     * 
     * @return 상대방 이름
     */
    public String getOtherMemberName() {
        // 알림을 받는 회원이 호스트인 경우, 매칭을 요청한 멤버의 이름을 반환
        if (this.matching.getHost().getMember().getId().equals(this.member.getId())) {
            return this.matching.getMember().getName();
        }
        // 알림을 받는 회원이 멤버인 경우, 호스트의 이름을 반환
        else {
            return this.matching.getHost().getMember().getName();
        }
    }
    
    /**
     * 알림을 읽음 처리합니다.
     */
    public void markAsRead() {
        this.isRead = true;
    }
}