package com.jimjim.lugeasy.auth.service.impl;

import com.jimjim.lugeasy.auth.service.AuthenticationService;
import com.jimjim.lugeasy.auth.errorCode.AuthErrorCode;
import com.jimjim.lugeasy.common.error.RestApiException;
import com.jimjim.lugeasy.user.domain.Member;
import com.jimjim.lugeasy.user.domain.SocialType;
import com.jimjim.lugeasy.user.domain.PermissionRole;
import com.jimjim.lugeasy.user.infrastructure.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthenticationServiceImpl implements AuthenticationService {

    private final MemberRepository memberRepository;

    @Override
    public Member createNewMember(String clientId, SocialType socialType) {
        // 인증 관련 회원 생성 로직
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
                .orElseThrow(() -> new RestApiException(AuthErrorCode.INVALID_MEMBER_DATA));
    }

    @Override
    public void deleteMember(Member member) {
        // 인증 관련 회원 탈퇴 로직
        memberRepository.delete(member);
    }
} 