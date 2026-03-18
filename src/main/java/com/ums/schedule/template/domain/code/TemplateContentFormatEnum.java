package com.ums.schedule.template.domain.code;

import com.ums.schedule.common.code.EnumMapperType;

public enum TemplateContentFormatEnum implements EnumMapperType {

    TEXT("TEXT", "완성형"),
    HTML("HTML", "파일형");

    String value;
    String description;

    TemplateContentFormatEnum(String value, String description) {
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
