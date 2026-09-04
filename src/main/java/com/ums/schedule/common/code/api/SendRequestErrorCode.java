package com.ums.schedule.common.code.api;

public enum SendRequestErrorCode implements ErrorCode {
    SCHEDULE_REQUIRED(ApiResponseCode.BAD_REQUEST, "스케쥴 ID는 필수 값 입니다."),
    TEMPLATE_KEY_REQUIRED(ApiResponseCode.BAD_REQUEST, "템플릿 키는 필수 값 입니다."),
    SENDER_KEY_REQUIRED(ApiResponseCode.BAD_REQUEST, "발신 키는 필수 값 입니다."),
    CUSTOMER_KEY_REQUIRED(ApiResponseCode.BAD_REQUEST, "고객 요청 키는 필수 값 입니다."),
    REQUEST_CREATED_SUCCESS(ApiResponseCode.SUCCESS, "발송 요청이 정상 처리 되었습니다."),
    DUPLICATED_CUSTOMER_KEY(ApiResponseCode.CONFLICT, "이미 존재하는 고객 키 입니다."),

    SEND_REQUEST_CREATED(ApiResponseCode.CONFLICT, "최초 생성된 발송 요청 건입니다."),
    SEND_REQUEST_READY(ApiResponseCode.CONFLICT, "대상자 업로드가 완료된 요청 건입니다."),
    SEND_REQUEST_WAITING(ApiResponseCode.CONFLICT, "대상자 업로드 대기 상태 입니다."),
    SEND_REQUEST_SENDING(ApiResponseCode.CONFLICT, "발송 중인 요청 건입니다."),
    SEND_REQUEST_COMPLETED(ApiResponseCode.CONFLICT, "발송이 완료된 상태입니다."),
    SEND_REQUEST_FAILED(ApiResponseCode.CONFLICT, "발송이 실패된 건 입니다.."),
    SEND_REQUEST_PAUSE(ApiResponseCode.CONFLICT, "일시 중지 된 발송입니다."),
    SEND_REQUEST_STOP(ApiResponseCode.CONFLICT, "중지 된 발송입니다."),
    SEND_REQUEST_REQUESTED(ApiResponseCode.CONFLICT, "이미 발송이 요청되었습니다."),
    SEND_REQUEST_CANCELED(ApiResponseCode.CONFLICT, "취소된 발송 요청 건입니다."),
    ;

    ApiResponseCode responseCode;
    String message;

    SendRequestErrorCode(ApiResponseCode responseCode, String message) {
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

    @Override
    public ApiResponseCode responseCode() {
        return responseCode;
    }
}
