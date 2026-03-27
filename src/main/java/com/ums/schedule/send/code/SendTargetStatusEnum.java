package com.ums.schedule.send.code;

import com.ums.schedule.common.code.EnumMapperType;

public enum SendTargetStatusEnum implements EnumMapperType {
    CREATED("CRT", ""),
    READY("RDY", ""),
    RETRYING("RTY", ""),
    FAIL("FAIL", ""),
    COMPLETED("CMP", "완료")
    ;

    String value;
    String description;

    SendTargetStatusEnum(String value, String description) {
        this.value = value;
        this.description = description;
    }

    @Override
    public String code() {
        return null;
    }

    @Override
    public String value() {
        return null;
    }

    @Override
    public String description() {
        return null;
    }
}
