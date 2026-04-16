package com.ums.schedule.code.send;

import com.ums.schedule.code.EnumMapperType;

public enum SendRequestStatusEnum implements EnumMapperType {
    CREATE("CRT", "발송 요청 등록"),
    PENDING("PDG", "업로드 대기"),
    REQUEST("REQ", "발송요청"),
    READY("RDY", "발송 준비"),
    SCHEDULED("SCH", "스케쥴링"),
    SENDING("SND", "발송중"),
    COMPLETED("CMT", "발송완료")
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