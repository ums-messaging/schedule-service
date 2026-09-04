package com.ums.schedule.common.code.email;


import com.ums.schedule.common.code.mapper.EnumMapperType;

public enum EmailTemplateFormat implements EnumMapperType {

    TEXT("TEXT", "완성형"),
    HTML("HTML", "HTML형"),
    FILE("FILE", "파일형");

    String value;
    String description;

    EmailTemplateFormat(String value, String description) {
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
