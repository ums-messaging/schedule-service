package com.ums.schedule.common.code.email;

import com.ums.schedule.common.code.mapper.EnumMapperType;

public enum ConvertType implements EnumMapperType {
    PDF("PDF", "pdf"),
    HTML("HTML", "html"),
    NONE("NONE", "");

    String value;
    String description;

    ConvertType(String value, String description) {
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
