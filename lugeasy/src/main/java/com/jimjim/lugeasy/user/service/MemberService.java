package com.jimjim.lugeasy.user.service;

import com.jimjim.lugeasy.user.domain.Member;

public interface MemberService {
    
    /**
     * 회원 정보를 업데이트합니다
     */
    Member updateMember(Member member);
    
    /**
     * 회원 정보를 조회합니다
     */
    Member getMemberInfo(Long memberId);
} 