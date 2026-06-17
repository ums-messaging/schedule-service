package com.ums.schedule.domain.sendrequest.target.upload.code;

import com.ums.schedule.common.code.mapper.EnumMapperType;

public enum TargetUploadFormatEnum implements EnumMapperType {
    CSV("csv", ""),
    EXCEL("xlsx", ""),
    ;

    String value;
    String description;

    TargetUploadFormatEnum(String value, String description) {
        this.value = value;
        this.description = description;
    }

    @Override
    public String code() {
        return name();
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
