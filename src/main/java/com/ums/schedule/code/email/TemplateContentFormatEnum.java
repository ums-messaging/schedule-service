package com.ums.schedule.code.email;


import com.ums.schedule.code.EnumMapperType;

public enum TemplateContentFormatEnum implements EnumMapperType {

    TEXT("TEXT", "완성형"),
    HTML("HTML", "HTML형"),
    FILE("FILE", "파일형");

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
