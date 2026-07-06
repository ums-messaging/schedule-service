package com.ums.schedule.domain.sendrequest.template.code;


import com.ums.schedule.common.code.mapper.EnumMapperType;

public enum TemplateTypeEnum implements EnumMapperType {
    ADVERTISE("AD", "광고"),
    NONE("NONE", "해당없음");

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
