package com.jimjim.lugeasy.user.domain;

import com.jimjim.lugeasy.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ColumnDefault("false")
    private Boolean isHost;

    @Column(nullable = false)
    private String name;

    private String phoneNumber;

    @Column(nullable = false)
    private String email;

    private String profileImageUrl;

    @Column(nullable = false)
    private SocialType socialType;

    @Column(nullable = false)
    private String socialId;



}
