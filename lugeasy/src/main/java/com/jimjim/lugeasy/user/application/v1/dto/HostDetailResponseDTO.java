package com.jimjim.lugeasy.user.application.v1.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "호스트 상세 정보 DTO")
public class HostDetailResponseDTO {
    @Schema(description = "호스트 ID")
    private Long id;
    
    @Schema(description = "호스트 이름")
    private String name;
    
    @Schema(description = "호스트 소개")
    private String introduce;
    
    @Schema(description = "평균 평점")
    private Double averageRating;
    
    @Schema(description = "리뷰 개수")
    private Long reviewCount;
    
    @Schema(description = "주소")
    private String address;
}
