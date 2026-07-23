package com.ums.schedule.common.code.api;

public enum SecurityMailErrorCode implements ErrorCode {
    OWNER_PW_CONFIGURED_LOAD_FAILS(ApiResponseCode.SERVER_ERROR, "관리자 비밀번호 설정을 가져오는데 실패했습니다.")

    ;

    ApiResponseCode code;
    String message;

    SecurityMailErrorCode(ApiResponseCode code, String message) {
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
        return null;
    }

    @Override
    public ApiResponseCode responseCode() {
        return code;
    }
}
