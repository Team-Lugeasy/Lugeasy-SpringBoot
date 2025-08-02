package com.jimjim.lugeasy.common.security;

import com.jimjim.lugeasy.common.error.RestApiException;
import com.jimjim.lugeasy.user.errorCode.MemberErrorCode;
import com.jimjim.lugeasy.user.infrastructure.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JpaUserDetailService implements UserDetailsService {
    private final MemberRepository memberRepository;
    @Override
    public CustomUserDetails loadUserByUsername(String memberId) {
        return memberRepository.findById(Long.valueOf(memberId))
                .map(CustomUserDetails::new).orElseThrow(
                        () -> new RestApiException(MemberErrorCode.MEMBER_NOT_FOUND)
                );
    }
}
