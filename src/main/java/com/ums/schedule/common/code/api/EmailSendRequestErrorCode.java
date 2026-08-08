package com.ums.schedule.common.code.api;

import com.ums.schedule.common.code.mapper.EnumMapperType;

public enum EmailSendRequestErrorCode implements EnumMapperType {
    TITLE_REQUIRED(ApiResponseCode.BAD_REQUEST, "제목은 필수 값 입니다."),
    INVALID_SENDER_EMAIL(ApiResponseCode.BAD_REQUEST, "발신자 키는 이메일 형식이어야 합니다."),
    ATTACHMENT_NAME_REQUIRED(ApiResponseCode.BAD_REQUEST, "첨부파일 명은 필수 값 입니다."),
    DOWNLOAD_NAME_REQUIRED(ApiResponseCode.BAD_REQUEST, "다운로드 명은 필수 값 입니다."),
    PASSWORD_POLICY_REQUIRED(ApiResponseCode.BAD_REQUEST, "비밀번호 정책은 필수 값 입니다."),
    SECURITY_POLICY_NOT_NULL(ApiResponseCode.BAD_REQUEST, "보안 정책 입력은 필수 값 입니다.")
    ,
    ;

    ApiResponseCode responseCode;
    String message;

    EmailSendRequestErrorCode(ApiResponseCode responseCode, String message) {
        this.responseCode = responseCode;
        this.message = message;
    }


    @Override
    public String code() {
        return responseCode.code();
    }

    @Override
    public String value() {
        return this.name();
    }

    @Override
    public String description() {
        return this.message;
    }


}
