package com.jimjim.lugeasy.common.error;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RestApiException extends RuntimeException {
    private final ErrorCodeInterface errorCode;

    public ErrorCode getErrorCode() {
        return this.errorCode.getErrorCode();
    }
}
