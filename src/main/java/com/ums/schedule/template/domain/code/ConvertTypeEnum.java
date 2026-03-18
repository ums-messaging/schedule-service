package com.ums.schedule.template.domain.code;

import com.ums.schedule.common.code.EnumMapperType;

public enum ConvertTypeEnum implements EnumMapperType {
    PDF("PDF", "PDF 변환"),
    HTML("HTML", "HTML 변환"),
    NONE("NONE", "변환 없음");

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
