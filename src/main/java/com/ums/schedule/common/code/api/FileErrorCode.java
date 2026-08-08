package com.ums.schedule.common.code.api;

public enum FileErrorCode implements ErrorCode {
    FILE_GENERATED_FAIL(ApiResponseCode.SERVER_ERROR, "파일 경로를 생성하는데 실패했습니다."),
    FILE_NOT_FOUND(ApiResponseCode.SERVER_ERROR, "[{0}] 파일이 존재하지 않습니다."),
    FILE_METADATA_LOAD_FAILED(ApiResponseCode.SERVER_ERROR, "[{0}] 파일 메타 정보를 조회하는데 실패했습니다. {1}}"),
    UPLOAD_URL_GENERATED_FAIL(ApiResponseCode.SERVER_ERROR, "[{0}] 업로드 URL 생성 중 오류가 발생했습니다. {1}"),
    FILE_UPLOAD_FAIL(ApiResponseCode.SERVER_ERROR, "[{0}] 파일 업로드 중 오류가 발생했습니다. {1}")
    ;


    ApiResponseCode code;
    String message;

    FileErrorCode(ApiResponseCode code, String message) {
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
