package com.ums.schedule.common.code.email;

import com.ums.schedule.common.code.mapper.EnumMapperType;

public enum EmailRequiredValue implements EnumMapperType {
    BODY_TEMPLATE_KEY("EMAIL_BODY", "이메일 본문 키"),
    BODY_TEMPLATE("BODY_TEMPLATE", "이메일 본문 내용")

    ;

    String value;
    String description;

    EmailRequiredValue(String value, String description) {
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
