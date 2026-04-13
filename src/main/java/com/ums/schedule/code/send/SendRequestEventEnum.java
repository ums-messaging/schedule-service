package com.ums.schedule.code.send;

import com.ums.schedule.code.EnumMapperType;

public enum SendRequestEventEnum implements EnumMapperType {
    JOB_CREATED("CREATED", "JOB 생성"),
    SEND_REQUEST("REQUESTED", "사용자 발송 요청"),
    SCHEDULING("SCHEDULED", "스케쥴링"),
    MESSAGE_CREATED("MESSAGE", "메시지 생성 완료"),
    TARGET_UPLOADED("UPLOADED", "대상자 업로드 완료"),
    MESSAGE_MAKING("MAKING", "대상자 메시지 생성 중"),
    SEND_START("START", "발송 시작"),
    SENDING("SENDING", "발송중"),
    SEND_END("COMPLETED", "발송 종료");

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