package com.jimjim.lugeasy.common.security.jwt.service;

import com.jimjim.lugeasy.auth.domain.JwtToken;
import com.jimjim.lugeasy.common.security.jwt.dto.TokenInfo;
import com.jimjim.lugeasy.user.domain.Member;
import com.jimjim.lugeasy.user.domain.PermissionRole;
import org.springframework.security.core.Authentication;

public interface JwtService {
        Boolean checkJwt(String token);

        JwtToken issueJwtToken(Member member);

        TokenInfo renewAccessToken(Long memberId, String clientId, PermissionRole permissionRole, JwtToken jwtToken);

        Authentication getAuthentication(String token);
        Long getMemberId(String token);
        String getClientId(String token);
        String getPermissionRole(String token);
        String getTokenType(String token);
        Boolean isExpired(String token);
    }
