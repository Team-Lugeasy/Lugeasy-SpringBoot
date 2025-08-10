package com.jimjim.lugeasy.user.service;

import com.jimjim.lugeasy.user.application.v1.dto.HostListResponseDTO;

import java.util.List;

public interface HostService {
    
    /**
     * 인증된 호스트 목록을 조회합니다.
     * 각 호스트의 이름, 평균 평점, 리뷰 개수, 주소를 포함합니다.
     */
    List<HostListResponseDTO> getAuthenticatedHostList();
}
