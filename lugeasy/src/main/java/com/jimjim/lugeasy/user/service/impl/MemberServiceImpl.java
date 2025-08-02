package com.jimjim.lugeasy.user.service.impl;

import com.jimjim.lugeasy.user.domain.Member;
import com.jimjim.lugeasy.user.infrastructure.MemberRepository;
import com.jimjim.lugeasy.user.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public Member updateMember(Member member) {
        // 회원 정보 업데이트 로직
        return memberRepository.save(member);
    }

    @Override
    public Member getMemberInfo(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));
    }
} 