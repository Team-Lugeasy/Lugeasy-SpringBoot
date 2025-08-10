package com.jimjim.lugeasy.user.application.v1.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HostAvailabilityResponseDTO {
    // 날짜(YYYY-MM-DD)를 키로, 해당 날짜의 사용 가능한 시간대(1-24) 리스트를 값으로 가집니다.
    private Map<String, List<Integer>> availability;
}
