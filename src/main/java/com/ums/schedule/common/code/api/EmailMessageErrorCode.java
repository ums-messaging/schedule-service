package com.ums.schedule.common.code.api;

public enum EmailMessageErrorCode implements ErrorCode {
    NOT_CONVERT_MESSAGE(ApiResponseCode.SERVER_ERROR, "메시지 첨부파일 변환 중 오류가 발생했습니다. (원인 : {0})"),
    NOT_FOUND_MESSAGE(ApiResponseCode.BAD_REQUEST, "[{0}] 이메일 메시지 정보가 존재하지 않습니다. "),
    SECURITY_POLICY_REQUIRED(ApiResponseCode.BAD_REQUEST, "보안 정책 '{0}'은 필수 값입니다."),
    NOT_FOUND_FILE_KEY(ApiResponseCode.SERVER_ERROR, "[{0}] 메시지 파일 키가 존재하지 않습니다."),
    NOT_FOUND_CONTENT(ApiResponseCode.SERVER_ERROR, "[{0}][{1}] 메시지 내용이 존재하지 않습니다."),
    NOT_SUPPORTED_CONVERT_TYPE(ApiResponseCode.SERVER_ERROR, "지원하지 않는 변환타입 입니다."),
    EMAIL_TITLE_REQUIRED(ApiResponseCode.SERVER_ERROR, "이메일 제목은 필수 값 입니다."),
    NOT_CONFIGURED_FILE_KEY_TEMPLATE(ApiResponseCode.SERVER_ERROR, "첨부파일로 변환해 업로드 할 파일 키 템플릿 정보를 불러들일 수 없습니다.")

    ;

    ApiResponseCode code;
    String message;

    EmailMessageErrorCode(ApiResponseCode code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String code() {
        return code.code();
    }

    @Override
    public String value() {
        return code.name();
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
