package com.jimjim.lugeasy.match.errorCode;

import com.jimjim.lugeasy.common.error.ErrorCode;
import com.jimjim.lugeasy.common.error.ErrorCodeInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MatchErrorCode implements ErrorCodeInterface {

    MATCH_NOT_FOUND("MATCH001", "매칭을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    MATCH_CANCEL_FAIL("MATCH002", "매칭 취소에 실패했습니다. 관리자에게 문의하세요.", HttpStatus.BAD_REQUEST),
    MATCH_NOT_OWNED("MATCH003", "본인의 매칭이 아닙니다.", HttpStatus.FORBIDDEN),
    MATCH_CANNOT_CANCEL("MATCH004", "취소할 수 없는 매칭 상태입니다.", HttpStatus.BAD_REQUEST),
    HOST_NOT_FOUND("MATCH005", "호스트를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    INVALID_TIME_RANGE("MATCH006", "짐 맡기기 시간은 짐 찾기 시간보다 이전이어야 합니다.", HttpStatus.BAD_REQUEST),
    MATCH_STATUS_UPDATE_FAILED("MATCH007", "매칭 상태 변경 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_STATUS_TRANSITION("MATCH008", "유효하지 않은 상태 변경입니다.", HttpStatus.BAD_REQUEST),
    CANNOT_CANCEL_AFTER_SERVICE_START("MATCH009", "서비스 시작 후에는 취소할 수 없습니다.", HttpStatus.BAD_REQUEST),
    CANNOT_COMPLETE_BEFORE_SERVICE_END("MATCH010", "서비스 종료 전에는 완료 처리할 수 없습니다.", HttpStatus.BAD_REQUEST),
    CANNOT_MODIFY_FINAL_STATUS("MATCH011", "완료되거나 거절된 매칭은 변경할 수 없습니다.", HttpStatus.BAD_REQUEST),
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
