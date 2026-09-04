package com.ums.schedule.common.code.api;

public enum EnumMapperErrorCode implements ErrorCode {
    INVALID_CODE(ApiResponseCode.BAD_REQUEST, "[{0}][{1}] 유효하지 않은 코드 값 입니다."),
    INVALID_KEY(ApiResponseCode.SERVER_ERROR, "[{0}] 유효하지 않은 코드 키 입니다.")
    ;

    ApiResponseCode code;
    String message;

    EnumMapperErrorCode(ApiResponseCode code, String message) {
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
        return this.message;
    }

    @Override
    public ApiResponseCode responseCode() {
        return code;
    }
}
