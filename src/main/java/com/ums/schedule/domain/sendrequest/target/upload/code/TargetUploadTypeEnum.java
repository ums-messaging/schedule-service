package com.ums.schedule.domain.sendrequest.target.upload.code;

import com.ums.schedule.common.code.mapper.EnumMapperType;

public enum TargetUploadTypeEnum implements EnumMapperType {
    FILE("F", "파일 업로드"),
    JSON("J", "JSON");

    String value;
    String description;

    TargetUploadTypeEnum(String value, String description) {
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
