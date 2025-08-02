package com.jimjim.lugeasy.user.service;

import com.jimjim.lugeasy.user.domain.Member;
import com.jimjim.lugeasy.user.domain.SocialType;

public interface MemberService {
    
    Member saveNewMember(String clientId, SocialType socialType);
    
    Member findMember(Long memberId);
    
    void resignMember(Member member);
} 