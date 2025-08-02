package com.jimjim.lugeasy.auth.errorCode;

import com.jimjim.lugeasy.common.error.ErrorCode;
import com.jimjim.lugeasy.common.error.ErrorCodeInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AuthErrorCode implements ErrorCodeInterface {
    INVALID_TOKEN("AUTH001", "유효하지 않은 토큰입니다.", HttpStatus.UNAUTHORIZED),
    EXPIRED_TOKEN("AUTH002", "만료된 토큰입니다.", HttpStatus.UNAUTHORIZED),
    UNSUPPORTED_TOKEN("AUTH003", "지원되지 않는 토큰입니다.", HttpStatus.UNAUTHORIZED),
    WRONG_TOKEN_SIGNATURE("AUTH004", "토큰의 서명이 잘못됐습니다.", HttpStatus.UNAUTHORIZED),
    EMPTY_TOKEN("AUTH005", "토큰이 비어있습니다.", HttpStatus.UNAUTHORIZED),
    INVALID_TOKEN_TYPE("AUTH006", "유효하지 않은 토큰 타입입니다.", HttpStatus.UNAUTHORIZED),
    EXPIRED_SIGNUP_TOKEN("AUTH007", "만료된 회원가입 토큰입니다.", HttpStatus.UNAUTHORIZED),
    NOT_FOUND_REFRESH_TOKEN("AUTH008", "Refresh Token을 찾을 수 없습니다. 다시 로그인하십시오.", HttpStatus.UNAUTHORIZED),
    EXPIRED_REFRESH_TOKEN("AUTH009", "만료된 Refresh Token입니다. 재발급을 위해 다시 로그인하십시오.", HttpStatus.UNAUTHORIZED);
    ;
    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.builder()
                .code(code)
                .message(message)
                .httpStatus(httpStatus)
                .build();
    }
}
