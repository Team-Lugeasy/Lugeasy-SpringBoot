package com.jimjim.lugeasy.notification.errorCode;

import com.jimjim.lugeasy.common.error.ErrorCode;
import com.jimjim.lugeasy.common.error.ErrorCodeInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum NotificationErrorCode implements ErrorCodeInterface {
    NOTIFICATION_NOT_FOUND("NOTIFICATION001", "알림을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    NOTIFICATION_ACCESS_DENIED("NOTIFICATION002", "알림에 접근할 권한이 없습니다.", HttpStatus.FORBIDDEN),
    INVALID_NOTIFICATION_DATA("NOTIFICATION003", "잘못된 알림 데이터입니다.", HttpStatus.BAD_REQUEST);
    
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