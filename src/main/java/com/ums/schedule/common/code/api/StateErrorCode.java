package com.ums.schedule.common.code.api;

public enum StateErrorCode implements ErrorCode {
    CANNOT_CHANGE_TO_STATE(ApiResponseCode.CONFLICT, "[{0} -> {1}] 상태로 변경 할 수 없습니다.")
    ;

    ApiResponseCode code;
    String message;

    StateErrorCode(ApiResponseCode code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String code() {
        return code.code();
    }

    @Override
    public String value() {
        return name();
    }

    @Override
    public String description() {
        return message;
    }

    @Override
    public ApiResponseCode responseCode() {
        return code;
    }
}
