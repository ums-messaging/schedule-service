package com.ums.schedule.common.code.api;

public enum AttachmentErrorCode implements ErrorCode {
    FILE_KEY_MAP_IS_NULL(ApiResponseCode.SERVER_ERROR, "file_key 혹은 file_key_template은 필수 값 입니다."),
    FILE_KEY_INFO_EMPTY(ApiResponseCode.SERVER_ERROR, "file_key와 file_key_template 모두 필수 값 입니다."),
    FILE_KEY_EMPTY(ApiResponseCode.SERVER_ERROR, "file_key는 필수 값 입니다."),
    INVALID_FILE_KEY_TEMPLATE(ApiResponseCode.BAD_REQUEST, "file_key에 치환 데이터 형식이 포함되어야 합니다."),
    FILE_KEY_TEMPLATE_EMPTY(ApiResponseCode.SERVER_ERROR, "file_key_template은 필수 값 입니다."),
    INVALID_FILE_INFO_FORMAT(ApiResponseCode.SERVER_ERROR, "{0}의 확장자는 {1}이어야 합니다."),
    FILE_SIZE_EMPTY(ApiResponseCode.SERVER_ERROR, "파일 크기는 공백일 수 없습니다."),
    NOT_SUPPORTED_CONVERT_TYPE(ApiResponseCode.BAD_REQUEST, "지원하지 않는 변환타입입니다."),
    ATTACHMENT_LIST_EMPTY(ApiResponseCode.BAD_REQUEST, "첨부 파일 목록이 존재하지 않습니다."),
    ATTACHMENT_UPLOAD_KEY_REQUIRED(ApiResponseCode.BAD_REQUEST, "첨부파일 키는 필수 값입니다.")
    ;

    ApiResponseCode code;
    String message;

    AttachmentErrorCode(ApiResponseCode code, String message) {
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
