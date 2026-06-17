package com.ums.schedule.domain.sendrequest.code;

import com.ums.schedule.common.converter.StatusStateEvent;

public enum SendRequestEventEnum implements StatusStateEvent {
    SEND_REQUEST_CREATED("CREATE", "발송 요청 생성 완료"),
    SEND_REQUEST_UPDATED("HOLDING", "발송 요청 수정 완료"),
    SEND_REQUEST_READY("READY", "발송 요청 준비 완료"),
    SEND_REQUEST_PAUSE("PAUSE", "발송 일시 중지 완료"),
    SEND_REQUEST_STOP("STOP", "발송 중지 완료"),
    SEND_REQUEST_RESUME("RESUME", "발송 재개"),
    SEND_REQUEST_REQUESTED("REQUEST", "발송 시작 요청 완료"),
    SEND_REQUEST_CANCELED("CANCEL", "발송 요청 취소 완료"),
    SEND_REQUEST_SEND_STARTED("SENDING", "발송 시작 완료"),
    SEND_REQUEST_SEND_FAIL("FAIL", "발송 실패"),
    SEND_REQUEST_SEND_COMPLETED("COMPLETED", "발송 완료"),
    SEND_REQUEST_SEND_RETRIED("RETRYING", "발송 재시도 완료")
    ;

    String value;
    String description;

    SendRequestEventEnum(String value, String description) {
        this.value = value;
        this.description = description;
    }

    @Override
    public String code() {
        return this.name();
    }

    @Override
    public String value() {
        return this.value;
    }

    @Override
    public String description() {
        return this.description;
    }
}
