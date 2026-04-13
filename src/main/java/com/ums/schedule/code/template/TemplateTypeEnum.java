package com.ums.schedule.code.template;


import com.ums.schedule.code.EnumMapperType;

public enum TemplateTypeEnum implements EnumMapperType {
    ADVERTISE("ADVERTISE", "광고");

    String value;
    String description;

    TemplateTypeEnum(String value, String description) {
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
