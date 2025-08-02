package com.jimjim.lugeasy.auth.service;

import com.jimjim.lugeasy.user.domain.Member;
import com.jimjim.lugeasy.user.domain.SocialType;

public interface AuthenticationService {
    
    /**
     * 새로운 회원을 생성합니다 (회원가입 로직)
     */
    Member createNewMember(String clientId, SocialType socialType);
    
    /**
     * 회원을 찾습니다
     */
    Member findMember(Long memberId);
    
    /**
     * 회원을 탈퇴시킵니다
     */
    void deleteMember(Member member);
} 