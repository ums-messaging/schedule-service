package com.ums.schedule.common.code.request;

import com.ums.schedule.common.converter.state.StatusStateEvent;
import com.ums.schedule.common.converter.state.StatusStateType;

public enum SendRequestEvent implements StatusStateEvent {
    SEND_REQUEST_CREATED(SendRequestStatus.CREATE, "발송 요청 생성 완료"),
    SEND_REQUEST_UPDATED(SendRequestStatus.HOLDING, "발송 요청 수정 완료"),
    SEND_REQUEST_READY(SendRequestStatus.READY, "발송 요청 준비 완료"),
    SEND_REQUEST_PAUSE(SendRequestStatus.PAUSE, "발송 일시 중지 완료"),
    SEND_REQUEST_REQUESTED(SendRequestStatus.REQUEST, "발송 시작 요청 완료"),
    SEND_REQUEST_CANCELED(SendRequestStatus.CANCEL, "발송 요청 취소 완료"),
    SEND_REQUEST_SEND_STARTED(SendRequestStatus.SENDING, "발송 시작 완료"),
    SEND_REQUEST_SEND_FAIL(SendRequestStatus.ERROR, "발송 실패"),
    SEND_REQUEST_SEND_COMPLETED(SendRequestStatus.COMPLETED, "발송 완료"),
//    SEND_REQUEST_SEND_RETRIED(SendRequestStatus., "발송 재시도 완료"),
//    SEND_REQUEST_STOP(SendRequestStatus.PAUSE, "발송 중지 완료"),
    SEND_REQUEST_RESUME(SendRequestStatus.SENDING, "발송 재개")
    ;

    SendRequestStatus status;
    String description;

    SendRequestEvent(SendRequestStatus status, String description) {
        this.status = status;
        this.description = description;
    }

    @Override
    public String code() {
        return this.name();
    }

    @Override
    public String value() {
        return this.status.code();
    }

    @Override
    public String description() {
        return this.description;
    }

    @Override
    public StatusStateType stateType() {
        return this.status;
    }
}
