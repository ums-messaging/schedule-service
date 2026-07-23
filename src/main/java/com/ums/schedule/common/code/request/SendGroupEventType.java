package com.ums.schedule.common.code.request;

import com.ums.schedule.common.converter.StatusStateEvent;
import com.ums.schedule.common.converter.StatusStateType;

public enum SendGroupEventType implements StatusStateEvent {
    SEND_GROUP_CREATED("CREATE", "발송 대상자 그룹 생성 완료"),
    SEND_GROUP_PREPARED("PREPARE", "발송 대상자 그룹 발송 준비 완료"),
    SEND_GROUP_SENDING("SENDING", "발송 대상자 그룹 발송 중"),
    SEND_GROUP_COMPLETED("COMPLETE", "발송 대상자 그룹 발송 완료"),
    SEND_GROUP_FAILED("FAIL", "발송 대상자 그룹 발송 실패"),
    SEND_GROUP_RETIRED("RETRYING", "발송 대상자 그룹 발송 재시도"),
    SEND_GROUP_STOPPED("FAIL", "발송 대상자 그룹 발송 중지"),
    SEND_GROUP_PAUSED("PAUSE", "발송 대상자 그룹 발송 일시 중지"),
    SEND_GROUP_RESUMED("RESUME", "발송 대상자 그룹 발송 재개")
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