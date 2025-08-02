package com.jimjim.lugeasy.auth.representation.v1;

import com.jimjim.lugeasy.auth.application.v1.AuthenticationFacade;
import com.jimjim.lugeasy.auth.application.v1.dto.AuthenticationRequest;
import com.jimjim.lugeasy.auth.application.v1.dto.AuthenticationResponse;
import com.jimjim.lugeasy.common.security.AuthenticationMember;
import com.jimjim.lugeasy.common.response.BaseResponse;
import com.jimjim.lugeasy.user.domain.Member;
import com.jimjim.lugeasy.user.domain.SocialType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auths")
@Tag(name = "(로그인) 멤버 API", description = "로그인 관련 멤버 API 입니다.")
public class AuthenticationController {

    private final AuthenticationFacade authenticationFacade;

    @Operation(summary = "회원 로그인/회원가입 API", description = "회원 로그인/회원가입 API 입니다.")
    @PostMapping("/sign-in")
    public BaseResponse<AuthenticationResponse.AuthSignIn> signIn(
            @RequestParam SocialType socialType,
            @RequestBody AuthenticationRequest.AuthSignIn request) {
        return BaseResponse.onSuccess(authenticationFacade.signIn(socialType, request));
    }

    @Operation(summary = "회원 탈퇴 API", description = "회원 탈퇴 API 입니다.")
    @DeleteMapping("/resign")
    public BaseResponse<AuthenticationResponse.AuthResign> resign(
            @AuthenticationMember Member member) {
        return BaseResponse.onSuccess(authenticationFacade.resign(member));
    }

    @Operation(summary = "엑세스 토큰 재발급 API", description = "엑세스 토큰 재발급 API 입니다.")
    @PostMapping("/refresh/accessToken")
    public BaseResponse<AuthenticationResponse.AuthRenewAccessToken> refreshAccessToken(
            @RequestBody AuthenticationRequest.AuthRefreshAccessToken request) {
        return BaseResponse.onSuccess(authenticationFacade.refreshAccessToken(request));
    }

}
