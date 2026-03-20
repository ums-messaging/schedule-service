package com.ums.schedule.attachment.code;

import com.ums.schedule.common.code.EnumMapperType;

public enum StorageTypeEnum implements EnumMapperType {
    S3("S3", "AWS");

    private String value;
    private String description;

    StorageTypeEnum(String value, String description) {
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
