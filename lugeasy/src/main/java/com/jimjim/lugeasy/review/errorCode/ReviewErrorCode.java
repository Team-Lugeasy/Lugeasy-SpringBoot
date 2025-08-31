package com.jimjim.lugeasy.review.errorCode;

import com.jimjim.lugeasy.common.error.ErrorCode;
import com.jimjim.lugeasy.common.error.ErrorCodeInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ReviewErrorCode implements ErrorCodeInterface {

    REVIEW_NOT_FOUND("REVIEW001", "리뷰를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    REVIEW_NOT_OWNED("REVIEW002", "본인의 리뷰가 아닙니다.", HttpStatus.FORBIDDEN),
    REVIEW_ALREADY_EXISTS("REVIEW003", "이미 리뷰를 작성했습니다.", HttpStatus.BAD_REQUEST),
    MATCH_NOT_COMPLETED("REVIEW004", "매칭이 완료되지 않아 리뷰를 작성할 수 없습니다.", HttpStatus.BAD_REQUEST),
    MATCH_NOT_FOUND("REVIEW005", "매칭을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    MATCH_NOT_OWNED("REVIEW006", "본인의 매칭이 아닙니다.", HttpStatus.FORBIDDEN),
    INVALID_RATING("REVIEW007", "평점은 1-5 사이의 값이어야 합니다.", HttpStatus.BAD_REQUEST),
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
