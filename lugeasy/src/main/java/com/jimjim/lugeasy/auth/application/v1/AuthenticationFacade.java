package com.jimjim.lugeasy.auth.application.v1;

import com.jimjim.lugeasy.auth.application.v1.dto.AuthenticationRequest;
import com.jimjim.lugeasy.auth.application.v1.dto.AuthenticationResponse;
import com.jimjim.lugeasy.auth.domain.JwtToken;
import com.jimjim.lugeasy.auth.infrastructure.JwtTokenRepository;
import com.jimjim.lugeasy.auth.mapper.AuthenticationMapper;
import com.jimjim.lugeasy.user.domain.Member;
import com.jimjim.lugeasy.user.domain.SocialType;
import com.jimjim.lugeasy.user.infrastructure.MemberRepository;
import com.jimjim.lugeasy.user.service.MemberService;
import com.jimjim.lugeasy.common.security.jwt.service.JwtService;
import com.jimjim.lugeasy.common.error.RestApiException;
import com.jimjim.lugeasy.auth.errorCode.AuthErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthenticationFacade {

    // JWT
    private final JwtTokenRepository jwtTokenRepository;
    private final JwtService jwtService;

    // Authentication
    private final AuthenticationMapper authenticationMapper;

    // Member
    private final MemberRepository memberRepository;
    private final MemberService memberService;

    /**
     * 로그인/회원가입 로직입니다.
     * 유저가 존재하지 않으면 회원가입을 진행합니다.
     * 기존 유저가 존재한다면 RefreshToken을 통해 자동 로그인을 진행합니다.
     * 이 때, 다중 기기 접속을 위해 기존 RefreshToken을 사용합니다.
     * 기존 RefreshToken이 만료되었다면, 재발급합니다.
     *
     * @param socialType (KAKAO, GOOGLE, NAVER, APPLE)
     * @param request MemberSignInByEncryptedUserIdentifier {
     *         String encryptedUserIdentifier;
     *     }
     * @return MemberSignIn {
     *         Long memberId;
     *         String accessToken;
     *         String refreshToken;
     *         Boolean isMembered;
     *         String nickname;
     *     }
     */
    @Transactional
    public AuthenticationResponse.AuthSignIn signIn(SocialType socialType, AuthenticationRequest.AuthSignIn request) {
        String clientId = request.getEncryptedUserIdentifier();
        Optional<Member> optionalMember = memberRepository.findByClientId(clientId);

        // 해당 유저가 존재하지 않으면, Member 객체 생성하고 DB에 저장 -> 회원가입
        if (optionalMember.isEmpty()) {
            // 새로운 유저를 만들고 디비에 저장 & JWT Token 생성
            Member newMember = memberService.saveNewMember(clientId, socialType);
            JwtToken newJwtToken = jwtService.issueJwtToken(newMember);
            return authenticationMapper.toAuthSignIn(newMember, newJwtToken, false);
        }
        // 멤버의 기존 토큰이 없으면 새로 생성 후 저장. 있다면 기존 것 불러오기
        // 로그인마다 갱신하지 않고 기존 토큰을 사용하는 이유는 다중 기기 접속을 허용하기 위함.
        Member member = optionalMember.get();
        JwtToken jwtToken = jwtTokenRepository.findById(member.getId())
                .orElseGet(() -> {
                    return jwtService.issueJwtToken(member);
                });
        // 기존 RefreshToken이 유효하지 않으면 새로 갱신
        if (jwtService.isExpired(jwtToken.getRefreshToken())) {
            jwtToken = jwtService.issueJwtToken(member);
        }

        return authenticationMapper.toAuthSignIn(member, jwtToken, true);
    }

    public AuthenticationResponse.AuthResign resign(Member member) {
        Member resignMember = memberService.findMember(member.getId());

        memberService.resignMember(resignMember);
        return authenticationMapper.toAuthResign(resignMember);
    }

    public AuthenticationResponse.AuthRenewAccessToken refreshAccessToken(AuthenticationRequest.AuthRefreshAccessToken request) {
        String refreshToken = request.getRefreshToken();
        
        // 리프레시 토큰에서 사용자 정보 추출
        String clientIdFromRefreshToken = jwtService.getClientId(refreshToken);
        Long memberIdFromRefreshToken = jwtService.getMemberId(refreshToken);
        
        // 해당 사용자 찾기
        Member member = memberRepository.findByClientId(clientIdFromRefreshToken)
                .orElseThrow(() -> new RestApiException(AuthErrorCode.INVALID_TOKEN));
        
        // DB에서 저장된 JWT 토큰 정보 가져오기
        JwtToken jwtToken = jwtTokenRepository.findById(memberIdFromRefreshToken)
                .orElseThrow(() -> new RestApiException(AuthErrorCode.NOT_FOUND_REFRESH_TOKEN));
        
        // 리프레시 토큰 유효성 검사
        if (jwtService.isExpired(refreshToken)) {
            throw new RestApiException(AuthErrorCode.EXPIRED_TOKEN);
        }
        
        // DB에 저장된 리프레시 토큰과 요청된 리프레시 토큰이 일치하는지 확인
        if (!jwtToken.getRefreshToken().equals(refreshToken)) {
            throw new RestApiException(AuthErrorCode.INVALID_TOKEN);
        }
        
        // 액세스 토큰 재발급
        return authenticationMapper.toAuthRenewAccessToken(memberIdFromRefreshToken, jwtToken.getAccessToken(), jwtToken.getRefreshToken());
    }
}
