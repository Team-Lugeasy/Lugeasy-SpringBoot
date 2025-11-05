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
@Schema(description = "호스트 목록 응답 DTO")
public class HostListResponseDTO {
    @Schema(description = "호스트 ID")
    private Long id;
    
    @Schema(description = "호스트 이름")
    private String name;
    
    @Schema(description = "리뷰 평점 평균")
    private Double reviewRate;
    
    @Schema(description = "리뷰 개수")
    private Long reviewCount;
    
    @Schema(description = "주소")
    private String address;
    
    @Schema(description = "위도")
    private Double latitude;
    
    @Schema(description = "경도")
    private Double longitude;
}
