package com.ums.schedule.code.send;

import com.ums.schedule.code.EnumMapperType;

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
