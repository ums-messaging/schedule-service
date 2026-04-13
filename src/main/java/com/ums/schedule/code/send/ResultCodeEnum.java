package com.ums.schedule.code.send;

import com.ums.schedule.code.EnumMapperType;

public enum ResultCodeEnum implements EnumMapperType {
    SUCCESS("OK", "처리 성공"),
    FAIL("ERR", "처리 에러"),
    RETRY("RTY", "재시도");
    String value;
    String description;

    ResultCodeEnum(String value, String description) {
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
