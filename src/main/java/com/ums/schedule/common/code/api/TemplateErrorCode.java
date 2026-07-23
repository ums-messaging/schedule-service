package com.ums.schedule.common.code.api;

public enum TemplateErrorCode implements ErrorCode {
    TEMPLATE_PARSE_FAIL(ApiResponseCode.CONFLICT, "[{0}][{1}]템플릿 치환에 실패했습니다. "),
    TEMPLATE_LOAD_FAIL(ApiResponseCode.SERVER_ERROR, "템플릿 로드에 실패했습니다. {0}"),
    TEMPLATE_NOT_CONFIGURED(ApiResponseCode.SERVER_ERROR, "[{0}][{1}] 템플릿 정보를 불러들이는데 실패했습니다."),

    TEMPLATE_CONTENT_EMPTY(ApiResponseCode.SERVER_ERROR, "[{0}] 템플릿 정보가 존재하지 않습니다."),
    FILE_KEY_TEMPLATE_EMPTY(ApiResponseCode.SERVER_ERROR, "[{0}] 파일 키 템플릿이 존재하지 않습니다.")
    ;


    ApiResponseCode code;
    String message;

    TemplateErrorCode(ApiResponseCode code, String message) {
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
