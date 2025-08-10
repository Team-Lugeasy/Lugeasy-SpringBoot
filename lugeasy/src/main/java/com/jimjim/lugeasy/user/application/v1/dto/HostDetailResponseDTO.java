package com.jimjim.lugeasy.user.application.v1.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HostDetailResponseDTO {
    private String name;           // 호스트 이름
    private String introduce;      // 한줄 소개
    private Double averageRating;  // 평점 평균
    private Long reviewCount;      // 리뷰 개수
    private String address;        // 주소
}
