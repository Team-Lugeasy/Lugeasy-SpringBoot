package com.jimjim.lugeasy.match.domain;

import com.jimjim.lugeasy.common.entity.BaseEntity;
import com.jimjim.lugeasy.user.domain.Host;
import com.jimjim.lugeasy.user.domain.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Matching extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_id", nullable = false)
    private Host host;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    private MatchingStatus matchingStatus;

    private LocalDateTime dropOffTime;

    private LocalDateTime findingTime;

    // host에 address가 있는데 주소/위도/경도 표시해야 하나?

    /**
     * 매칭 상태를 변경합니다.
     * 
     * @param newStatus 새로운 매칭 상태
     */
    public void updateMatchingStatus(MatchingStatus newStatus) {
        this.matchingStatus = newStatus;
    }
}
