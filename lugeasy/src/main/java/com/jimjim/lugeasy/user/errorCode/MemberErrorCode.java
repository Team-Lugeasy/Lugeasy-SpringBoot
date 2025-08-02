package com.jimjim.lugeasy.user.errorCode;

import com.jimjim.lugeasy.common.error.ErrorCode;
import com.jimjim.lugeasy.common.error.ErrorCodeInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MemberErrorCode implements ErrorCodeInterface {

    MEMBER_NOT_FOUND("MEMBER001", "Member가 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    MEMBER_RESIGN_FAIL("MEMBER002", "Member 삭제에 실패했습니다. 관리자에게 문의하세요.", HttpStatus.BAD_REQUEST),
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