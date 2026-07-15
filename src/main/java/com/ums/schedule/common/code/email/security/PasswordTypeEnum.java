package com.ums.schedule.common.code.email.security;

import com.ums.schedule.common.code.mapper.EnumMapperType;

public enum PasswordTypeEnum implements EnumMapperType {
    PASSWORD_POLICY("policy", "비밀번호 정책"),
    PASSWORD_FORMAT("format", "비밀번호 형식")
    ;

    String value;
    String description;

    PasswordTypeEnum(String value, String description) {
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
