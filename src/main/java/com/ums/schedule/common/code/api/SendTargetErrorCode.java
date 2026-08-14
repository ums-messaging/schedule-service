package com.ums.schedule.common.code.api;

public enum SendTargetErrorCode implements ErrorCode {
    TARGET_VARIABLE_REQUIRED(ApiResponseCode.BAD_REQUEST, "메시지 치환 변수가 존재하지 않습니다."),
    TARGET_ROW_READ_FAILS(ApiResponseCode.SERVER_ERROR, "[{0}] 대상자 로우를 읽는 중 오류가 발생했습니다. (원인 : {1})"),
    TARGET_UPLOAD_FAIL(ApiResponseCode.SERVER_ERROR, "[{0}] 대상자 업로드 중 오류가 발생했습니다. (원인 : {1})")

    ;

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
