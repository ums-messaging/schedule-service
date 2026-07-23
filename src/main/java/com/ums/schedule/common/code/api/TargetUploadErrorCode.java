package com.ums.schedule.common.code.api;

public enum TargetUploadErrorCode implements ErrorCode {
    TARGET_UPLOAD_LIMIT_EXCEEDED(ApiResponseCode.BAD_REQUEST, "업로드 대상자 건수가 허용 범위를 초과했습니다. (최대 허용 수 : {0},업로드 요청 수 : {1})"),
    TARGET_LIST_OF_EMPTY(ApiResponseCode.BAD_REQUEST, "업로드 할 대상자 목록이 존재하지 않습니다."),
    UNSUPPORTED_UPLOAD_TYPE(ApiResponseCode.BAD_REQUEST, "지원하지 않는 요청 타입입니다. (지원 타입 : {0})"),
    UNSUPPORTED_UPLOAD_FORMAT(ApiResponseCode.BAD_REQUEST, "지원되는 업로드 형식이 아닙니다. (지원 형식 : {0})"),

    UPLOAD_FILE_NOT_EXIST(ApiResponseCode.BAD_REQUEST, "[{0}] 업로드 파일 ({1})이 존재하지 않습니다."),


    DOWNLOAD_KEY_GENERATION_FAILED(ApiResponseCode.SERVER_ERROR, "다운로드 키 생성에 실패했습니다."),
    UPLOAD_KEY_GENERATION_FAILED(ApiResponseCode.SERVER_ERROR, "업로드 키 발급에 실패했습니다. {0}"),
    TARGET_UPLOAD_COUNT_MISMATCH(ApiResponseCode.SERVER_ERROR, "대상자 업로드 요청 수와 실제 업로드 수가 일치하지 않습니다. (업로드 요청 수 : {0}, 실제 업로드 수 {0})")

    ;

    ApiResponseCode code;
    String message;

    TargetUploadErrorCode(ApiResponseCode code, String message) {
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
