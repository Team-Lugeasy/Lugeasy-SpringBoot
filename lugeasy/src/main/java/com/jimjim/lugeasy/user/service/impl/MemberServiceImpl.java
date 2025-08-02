package com.jimjim.lugeasy.user.service.impl;

import com.jimjim.lugeasy.user.domain.Member;
import com.jimjim.lugeasy.user.domain.SocialType;
import com.jimjim.lugeasy.user.domain.PermissionRole;
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
    public Member saveNewMember(String clientId, SocialType socialType) {
        // 임시 구현 - 실제로는 더 복잡한 로직이 필요
        Member member = Member.builder()
                .clientId(clientId)
                .socialType(socialType)
                .name("임시 사용자")
                .email("temp@example.com") // 임시 이메일
                .socialId(clientId) // clientId를 socialId로 사용
                .permissionRole(PermissionRole.ROLE_CLIENT) // 기본 권한
                .build();
        return memberRepository.save(member);
    }

    @Override
    public Member findMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));
    }

    @Override
    public void resignMember(Member member) {
        // 임시 구현 - 실제로는 더 복잡한 로직이 필요
        memberRepository.delete(member);
    }
} 