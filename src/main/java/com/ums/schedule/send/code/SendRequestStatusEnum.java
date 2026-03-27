package com.ums.schedule.send.code;

import com.ums.schedule.common.code.EnumMapperType;

public enum SendRequestStatusEnum implements EnumMapperType {
    CREATE("CRT", "발송 요청 등록"),
    REQUEST("REQ", "발송요청"),
    READY("RDY", "발송 준비"),
    SCHEDULING("SCH", "스케쥴링"),
    SENDING("SND", "발송중"),
    RETRYING("RTY", "재시도 중"),
    SUCCESS("SUC", "발송성공"),
    FAILED("FLD", "발송실패")
    ;

    String value;
    String description;

    SendRequestStatusEnum(String value, String description) {
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