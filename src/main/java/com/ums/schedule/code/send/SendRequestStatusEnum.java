package com.ums.schedule.code.send;

import com.ums.schedule.code.EnumMapperType;

public enum SendRequestStatusEnum implements EnumMapperType {
    CREATE("CREATE", "발송 요청 등록"),
    HOLDING("HOLD", "업로드 대기"),
    REQUEST("REQ", "발송요청"),
    READY("READY", "발송준비"),
    SCHEDULED("SCH", "스케쥴링"),
    SENDING("SEND", "발송중"),
    COMPLETED("END", "발송완료"),
    ERROR("ERR", "발송에러");

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