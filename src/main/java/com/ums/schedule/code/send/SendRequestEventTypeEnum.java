package com.ums.schedule.code.send;

import com.ums.schedule.code.EnumMapperType;

public enum SendRequestEventTypeEnum implements EnumMapperType {
    REQUEST_CREATED("REQUEST_CREATED", "발송 요청 생성"),
    SEND_REQUESTED("SEND_REQUESTED", "사용자 발송 요청"),
    TARGET_UPLOAD_REQUEST("TARGET_UPLOAD_REQUESTED","대상자 업로드 요청"),
    TARGET_UPLOAD_COMPLETED("TARGET_UPLOAD_COMPLETED","대상자 업로드 완료"),
    JOB_CREATED("JOB_CREATED", "JOB 생성"),
    ERROR("ERR", "처리 오류"),
    SCHEDULED("SCHEDULED", "스케쥴링"),
    SEND_STARTED("MESSAGE", "메시지 생성 완료"),
    SEND_ENDED("COMPLETED", "발송 종료");

    String value;
    String description;

    SendRequestEventTypeEnum(String value, String description) {
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