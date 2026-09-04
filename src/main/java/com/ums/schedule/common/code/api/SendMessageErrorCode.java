package com.ums.schedule.common.code.api;

import com.ums.schedule.common.code.mapper.EnumMapperType;

public enum SendMessageErrorCode implements ErrorCode {
    ADVERTISE_PREFIX_NOT_CONFIGURED(ApiResponseCode.SERVER_ERROR, "광고 문구를 불러들이는데 실패하였습니다."),
    ;

    ApiResponseCode code;
    String message;

    SendMessageErrorCode(ApiResponseCode code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String code() {
        return this.code.code();
    }

    @Override
    public String value() {
        return this.name();
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
