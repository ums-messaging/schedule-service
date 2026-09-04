package com.ums.schedule.common.code.api;

public enum CommonErrorCode implements ErrorCode {
    CONFIGURATION_LOAD_FAIL(ApiResponseCode.SERVER_ERROR, "[{0}] 설정 정보를 불러들이는데 실패 했습니다."),
    NOT_SUPPORTED_TYPE(ApiResponseCode.CONFLICT, "[{0}] {1} 타입은 지원하지 않는 유형입니다."),
    REQUIRED_VALUE(ApiResponseCode.CONFLICT, "[{0}] {1}은 필수 값입니다."),
    DB_NOT_FOUND(ApiResponseCode.CONFLICT, "[{0}] 존재하지 않는 데이터입니다."),

    ;

    ApiResponseCode code;
    String message;

    CommonErrorCode(ApiResponseCode code, String message) {
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
