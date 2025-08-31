package com.jimjim.lugeasy.auth.representation.v1;

import com.jimjim.lugeasy.auth.application.v1.AuthenticationFacade;
import com.jimjim.lugeasy.auth.application.v1.dto.AuthenticationRequest;
import com.jimjim.lugeasy.auth.application.v1.dto.AuthenticationResponse;
import com.jimjim.lugeasy.common.security.AuthenticationMember;
import com.jimjim.lugeasy.common.response.BaseResponse;
import com.jimjim.lugeasy.user.domain.Member;
import com.jimjim.lugeasy.user.domain.SocialType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auths")
@Tag(name = "(로그인) 멤버 API", description = "로그인 관련 멤버 API 입니다.")
public class AuthenticationController {

    private final AuthenticationFacade authenticationFacade;

    @Operation(summary = "회원 로그인/회원가입 API", description = "소셜 로그인을 통해 회원 로그인/회원가입을 진행합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "로그인 성공",
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(
                    name = "로그인 성공 응답",
                    value = """
                    {
                      "timestamp": "2025-08-31T09:45:40.426159645Z",
                      "code": "COMMON200",
                      "message": "요청에 성공하였습니다.",
                      "result": {
                        "member_id": 16,
                        "access_token": "eyJhbGciOiJIUzUxMiJ9.eyJ0b2tlblR5cGUiOiJhY2Nlc3MiLCJtZW1iZXJJZCI6MTYsImNsaWVudElkIjoiZW5jcnlwdGVkVXNlcklkMTIzIiwicGVybWlzc2lvblJvbGUiOiJST0xFX0NMSUVOVCIsImlhdCI6MTc1NjYzMzU0MCwiZXhwIjoxNzU3NDk3NTQwfQ.FbZvYEKKtU8FZY1PjAXMuVcMar8QIA PULFSSv6Tzk0AdN2uKnM9Z3e0GKmp0e4YLjezSfgQ8C3bGNzwTD31WjA",
                        "refresh_token": "eyJhbGciOiJIUzUxMiJ9.eyJ0b2tlblR5cGUiOiJyZWZyZXNoIiwibWVtYmVySWQiOjE2LCJjbGllbnRJZCI6ImVuY3J5cHRlZFVzZXJJZDEyMyIsInBlcm1pc3Npb25Sb2xlIjoiUk9MRV9DTElFTlQiLCJpYXQi0jE3NTY2MzM1NDAsImV4cCI6MTc2ODcyOTU0MH0.FAErCkdTVwKiLZEFjDSOHYKwIBl0bFZPhZPHPI14VpFzw1iZ--07pZ5myLDR0WG-36FxbQu6LMk593XSZ8xkw",
                        "is_membered": true,
                        "name": "임시 사용자"
                      }
                    }
                    """
                )
            )
        )
    })
    @RequestBody(
        description = "로그인 요청",
        required = true,
        content = @Content(
            mediaType = "application/json",
            examples = @ExampleObject(
                name = "로그인 요청 예시",
                value = """
                {
                  "encrypted_user_identifier": "encrypted_user_identifier"
                }
                """
            )
        )
    )
    @PostMapping("/sign-in")
    public BaseResponse<AuthenticationResponse.AuthSignIn> signIn(
            @Parameter(description = "소셜 로그인 타입", example = "KAKAO", schema = @Schema(allowableValues = {"KAKAO", "GOOGLE", "NAVER", "APPLE"})) @RequestParam SocialType socialType,
            @RequestBody AuthenticationRequest.AuthSignIn request) {
        return BaseResponse.onSuccess(authenticationFacade.signIn(socialType, request));
    }

    @Operation(summary = "회원 탈퇴 API", description = "회원 탈퇴를 진행합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "탈퇴 성공",
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(
                    name = "탈퇴 성공 응답",
                    value = """
                    {
                      "timestamp": "2025-08-31T09:45:40.426159645Z",
                      "code": "COMMON200",
                      "message": "요청에 성공하였습니다.",
                      "result": {
                        "member_id": 16
                      }
                    }
                    """
                )
            )
        )
    })
    @DeleteMapping("/resign")
    public BaseResponse<AuthenticationResponse.AuthResign> resign(
            @AuthenticationMember Member member) {
        return BaseResponse.onSuccess(authenticationFacade.resign(member));
    }

    @Operation(summary = "액세스 토큰 재발급 API", description = "리프레시 토큰을 사용하여 새로운 액세스 토큰을 발급받습니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "토큰 재발급 성공",
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(
                    name = "토큰 재발급 성공 응답",
                    value = """
                    {
                      "timestamp": "2025-08-31T09:45:40.426159645Z",
                      "code": "COMMON200",
                      "message": "요청에 성공하였습니다.",
                      "result": {
                        "member_id": 16,
                        "access_token": "eyJhbGciOiJIUzUxMiJ9.eyJ0b2tlblR5cGUiOiJhY2Nlc3MiLCJtZW1iZXJJZCI6MTYsImNsaWVudElkIjoiZW5jcnlwdGVkVXNlcklkMTIzIiwicGVybWlzc2lvblJvbGUiOiJST0xFX0NMSUVOVCIsImlhdCI6MTc1NjYzMzU0MCwiZXhwIjoxNzU3NDk3NTQwfQ.FbZvYEKKtU8FZY1PjAXMuVcMar8QIA PULFSSv6Tzk0AdN2uKnM9Z3e0GKmp0e4YLjezSfgQ8C3bGNzwTD31WjA",
                        "refresh_token": "eyJhbGciOiJIUzUxMiJ9.eyJ0b2tlblR5cGUiOiJyZWZyZXNoIiwibWVtYmVySWQiOjE2LCJjbGllbnRJZCI6ImVuY3J5cHRlZFVzZXJJZDEyMyIsInBlcm1pc3Npb25Sb2xlIjoiUk9MRV9DTElFTlQiLCJpYXQi0jE3NTY2MzM1NDAsImV4cCI6MTc2ODcyOTU0MH0.FAErCkdTVwKiLZEFjDSOHYKwIBl0bFZPhZPHPI14VpFzw1iZ--07pZ5myLDR0WG-36FxbQu6LMk593XSZ8xkw"
                      }
                    }
                    """
                )
            )
        )
    })
    @RequestBody(
        description = "토큰 재발급 요청",
        required = true,
        content = @Content(
            mediaType = "application/json",
            examples = @ExampleObject(
                name = "토큰 재발급 요청 예시",
                value = """
                {
                  "refresh_token": "eyJhbGciOiJIUzUxMiJ9.eyJ0b2tlblR5cGUiOiJyZWZyZXNoIiwibWVtYmVySWQiOjE2LCJjbGllbnRJZCI6ImVuY3J5cHRlZFVzZXJJZDEyMyIsInBlcm1pc3Npb25Sb2xlIjoiUk9MRV9DTElFTlQiLCJpYXQi0jE3NTY2MzM1NDAsImV4cCI6MTc2ODcyOTU0MH0.FAErCkdTVwKiLZEFjDSOHYKwIBl0bFZPhZPHPI14VpFzw1iZ--07pZ5myLDR0WG-36FxbQu6LMk593XSZ8xkw"
                }
                """
            )
        )
    )
    @PostMapping("/refresh/accessToken")
    public BaseResponse<AuthenticationResponse.AuthRenewAccessToken> refreshAccessToken(
            @RequestBody AuthenticationRequest.AuthRefreshAccessToken request) {
        return BaseResponse.onSuccess(authenticationFacade.refreshAccessToken(request));
    }

}
