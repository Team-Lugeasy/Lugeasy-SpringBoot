package com.jimjim.lugeasy.common.security.jwt.service;

import com.jimjim.lugeasy.auth.domain.JwtToken;
import com.jimjim.lugeasy.auth.errorCode.AuthErrorCode;
import com.jimjim.lugeasy.auth.infrastructure.JwtTokenRepository;
import com.jimjim.lugeasy.common.error.RestApiException;
import com.jimjim.lugeasy.common.security.CustomUserDetails;
import com.jimjim.lugeasy.common.security.JpaUserDetailService;
import com.jimjim.lugeasy.common.security.jwt.JwtProperties;
import com.jimjim.lugeasy.common.security.jwt.dto.TokenInfo;
import com.jimjim.lugeasy.user.domain.Member;
import com.jimjim.lugeasy.user.domain.PermissionRole;
import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {
    private SecretKey secretKey;
    private final JwtProperties jwtProperties;
    private final JpaUserDetailService userDetailService;
    private final JwtTokenRepository jwtTokenRepository;

    @PostConstruct
    protected void init() {
        secretKey = new SecretKeySpec(jwtProperties.getJwt_secret().getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS512.key().build().getAlgorithm());
    }

    @Override
    public JwtToken issueJwtToken(Member member) {

       if (member.getClientId() == null || member.getPermissionRole() == null) {
             throw new RestApiException(AuthErrorCode.INVALID_MEMBER_DATA);
       }
        String accessToken = issueAccessToken(member.getId(), member.getClientId(), member.getPermissionRole());
        String refreshToken = issueRefreshToken(member.getId(), member.getClientId(), member.getPermissionRole());
        // 기존 토큰이 존재한다면 삭제 후 저장
        jwtTokenRepository.deleteById(member.getId());
        return jwtTokenRepository.save(new JwtToken(member.getId(), refreshToken, accessToken));
    }

    /**
     * 액세스 토큰 재발급 메서드
     *
     * @param memberId
     * @param clientId
     * @param permissionRole
     * @param jwtToken
     * @return
     */
    @Override
    @Transactional
    public TokenInfo renewAccessToken(Long memberId, String clientId, PermissionRole permissionRole, JwtToken jwtToken) {
        String newAccessToken = issueAccessToken(memberId, clientId, permissionRole);

        jwtToken.updateAccessToken(newAccessToken);
        jwtTokenRepository.save(jwtToken);

        return TokenInfo.builder()
                .refreshToken(jwtToken.getRefreshToken())
                .accessToken(jwtToken.getAccessToken())
                .build();
    }

    /**
     * 실제 액세스 토큰 발급
     *
     * @param memberId
     * @param clientId
     * @param permissionRole
     * @return
     */
    private String issueAccessToken(Long memberId, String clientId, PermissionRole permissionRole) {
        Long expiredTime = jwtProperties.getAccess_expired_time();

        return Jwts.builder()
                .claim("tokenType", "access")
                .claim("memberId", memberId)
                .claim("clientId", clientId)
                .claim("permissionRole", permissionRole)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredTime)) // 유효 기간 (1일)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    /**
     * 리프래쉬 토큰 발급
     *
     * @param memberId
     * @param clientId
     * @param permissionRole
     * @return
     */
    private String issueRefreshToken(Long memberId, String clientId, PermissionRole permissionRole) {
        Long expiredTime = jwtProperties.getRefresh_expired_time();

        return Jwts.builder()
                .claim("tokenType", "refresh")
                .claim("memberId", memberId)
                .claim("clientId", clientId)
                .claim("permissionRole", permissionRole)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredTime)) // 유효 기간 (14일)
                .signWith(SignatureAlgorithm.HS512, secretKey) //HS512알고리즘으로 key를 암호화
                .compact();
    }

    // 토큰 유효성 검사
    @Override
    public Boolean checkJwt(String token) { //TODO 토큰 유효성 검증 로직 수정 필요
        try {
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            throw new RestApiException(AuthErrorCode.INVALID_TOKEN);
        } catch (ExpiredJwtException e) {
            throw new RestApiException(AuthErrorCode.EXPIRED_TOKEN);
        } catch (UnsupportedJwtException e) {
            throw new RestApiException(AuthErrorCode.UNSUPPORTED_TOKEN);
        } catch (SignatureException e) {
            throw new RestApiException(AuthErrorCode.WRONG_TOKEN_SIGNATURE);
        } catch (IllegalArgumentException e) {
            throw new RestApiException(AuthErrorCode.EMPTY_TOKEN);
        }
    }

    @Override
    public Authentication getAuthentication(String token) {
        CustomUserDetails userDetails = userDetailService.loadUserByUsername(Long.toString(getMemberId(token)));
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    @Override
    public Long getMemberId(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("memberId", Long.class);
    }

    @Override
    public String getClientId(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("clientId", String.class);
    }

    @Override
    public String getPermissionRole(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("permissionRole", String.class);
    }

    @Override
    public String getTokenType(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("tokenType", String.class);
    }

    /**
     * 토큰이 만료됐는지 검사합니다.
     *
     * @param token JwtToken 객체에서 추출한 RefreshToken 또는 AccessToken
     * @return 만료 시 True / 유효 시 False
     */
    @Override
    public Boolean isExpired(String token) {
        if (token == null || token.trim().isEmpty()) {
            throw new RestApiException(AuthErrorCode.EMPTY_TOKEN); // ✅ 빈 토큰 예외 처리
        }
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getExpiration()
                    .before(new Date()); // ✅ 현재 시간과 만료 시간 비교
        } catch (JwtException e) {
            return true;  // ✅ 토큰이 만료된 경우 true 반환
        }
    }
}
