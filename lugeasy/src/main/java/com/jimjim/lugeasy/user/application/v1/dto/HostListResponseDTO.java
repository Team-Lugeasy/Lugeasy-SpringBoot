package com.jimjim.lugeasy.user.application.v1.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HostListResponseDTO {
    private Long id;               // 호스트 id
    private String name;           // Member.name
    private Double averageRating;  // 리뷰 rating의 평균값
    private Long reviewCount;      // 리뷰 개수
    private String address;        // Host.address
}
