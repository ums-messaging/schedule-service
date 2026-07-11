package com.ums.schedule.domain.message.email.code;

import com.ums.schedule.common.code.mapper.EnumMapperType;

public enum ConvertTypeEnum implements EnumMapperType {
    PDF("PDF", "pdf"),
    HTML("HTML", "html"),
    NONE("NONE", "");

    String value;
    String description;

    ConvertTypeEnum(String value, String description) {
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
