package com.ums.schedule.orchestrator.common.code;

import com.ums.schedule.common.code.mapper.EnumMapperType;

public enum UploadType implements EnumMapperType {
    FILE("FILE", "파일 업로드"),
    JSON("JSON", "JSON");

    String value;
    String description;

    UploadType(String value, String description) {
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
