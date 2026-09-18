package com.ums.schedule.common.code.request;

import com.ums.schedule.common.converter.state.StatusStateEvent;
import com.ums.schedule.common.converter.state.StatusStateType;

public enum SendGroupEventType implements StatusStateEvent {
    SEND_STARTED("SEND_STARTED", "발송 대상자 그룹 생성 완료"),
    SEND_COMPLETED("PREPARE", "발송 대상자 그룹 발송 준비 완료"),
    ;

    String value;
    String description;

    SendGroupEventType(String value, String description) {
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

    @Override
    public StatusStateType stateType() {
        return null;
    }
}