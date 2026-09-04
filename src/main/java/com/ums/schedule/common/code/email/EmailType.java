package com.ums.schedule.common.code.email;

import com.ums.schedule.common.code.mapper.EnumMapperType;

public enum EmailType implements EnumMapperType {
    PLAIN("PLAIN", "일반메일"),
    SECURITY("SECURITY", "보안메일")
    ;

    String value;
    String description;

    EmailType(String value, String description) {
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
