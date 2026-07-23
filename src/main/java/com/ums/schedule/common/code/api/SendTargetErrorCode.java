package com.ums.schedule.common.code.api;

public enum SendTargetErrorCode implements ErrorCode {
    TARGET_VARIABLE_REQUIRED(ApiResponseCode.BAD_REQUEST, "메시지 치환 변수가 존재하지 않습니다.");

    ApiResponseCode code;
    String message;

    SendTargetErrorCode(ApiResponseCode code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String code() {
        return code.code();
    }

    @Override
    public String value() {
        return this.name();
    }

    @Override
    public String description() {
        return this.message;
    }

    @Override
    public ApiResponseCode responseCode() {
        return code;
    }
}
