package com.jimjim.lugeasy.auth.mapper;

import com.jimjim.lugeasy.auth.application.v1.dto.AuthenticationResponse;
import com.jimjim.lugeasy.auth.domain.JwtToken;
import com.jimjim.lugeasy.user.domain.Member;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationMapper {

    public AuthenticationResponse.AuthSignIn toAuthSignIn(final Member member, JwtToken jwtToken, Boolean isMembered) {
        return AuthenticationResponse.AuthSignIn.builder()
                .memberId(member.getId())
                .isMembered(isMembered)
                .accessToken(jwtToken.getAccessToken())
                .refreshToken(jwtToken.getRefreshToken())
                .nickname(isMembered ? member.getName() : null)
                .build();
    }

    public AuthenticationResponse.AuthResign toAuthResign(Member member) {
        return AuthenticationResponse.AuthResign.builder()
                .memberId(member.getId())
                .build();
    }

    public AuthenticationResponse.AuthRenewAccessToken toAuthRenewAccessToken(Long memberId, String newAccessToken, String refreshToken) {
        return AuthenticationResponse.AuthRenewAccessToken.builder()
                .memberId(memberId)
                .accessToken(newAccessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
