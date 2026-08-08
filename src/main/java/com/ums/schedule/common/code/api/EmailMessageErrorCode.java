package com.ums.schedule.common.code.api;

public enum EmailMessageErrorCode implements ErrorCode {
    NOT_CONVERT_MESSAGE(ApiResponseCode.SERVER_ERROR, "메시지 첨부파일 변환 중 오류가 발생했습니다. (원인 : {0})"),
    NOT_EXECUTE_HANDLER(ApiResponseCode.CONFLICT, "메시지 변환 핸들러를 실행 할 수 없습니다. (CONVERT_TYPE : {0}, MAIL_TYPE : {1})"),
    NOT_FOUND_MESSAGE(ApiResponseCode.BAD_REQUEST, "[{0}] 이메일 메시지 정보가 존재하지 않습니다. "),
    SECURITY_POLICY_REQUIRED(ApiResponseCode.BAD_REQUEST, "보안 정책 '{0}'은 필수 값입니다."),
    NOT_FOUND_FILE_KEY(ApiResponseCode.SERVER_ERROR, "[{0}] 메시지 파일 키가 존재하지 않습니다."),
    NOT_FOUND_CONTENT(ApiResponseCode.SERVER_ERROR, "[{0}][{1}] 메시지 내용이 존재하지 않습니다."),
    NOT_SUPPORTED_CONVERT_TYPE(ApiResponseCode.SERVER_ERROR, "지원하지 않는 변환타입 입니다."),
    EMAIL_TITLE_REQUIRED(ApiResponseCode.SERVER_ERROR, "이메일 제목은 필수 값 입니다."),
    NOT_CONFIGURED_FILE_KEY_TEMPLATE(ApiResponseCode.SERVER_ERROR, "첨부 파일로 변환해 업로드 할 파일 키 템플릿 정보를 불러들일 수 없습니다."),
    NOT_FOUND_CONVERTED_CONTENT(ApiResponseCode.SERVER_ERROR,"변환할 템플릿 내용이 존재하지 않습니다."),
    INVALID_CONVERT_TYPE_FILE(ApiResponseCode.SERVER_ERROR, "변환할 파일 확장자가 유효하지 않습니다. (지원 타입 : {1})"),
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
