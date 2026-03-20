package com.ums.schedule.attachment.code;

import com.ums.schedule.common.code.EnumMapperType;

public enum EncryptionTypeEnum implements EnumMapperType {
    ASE128("ASE-128", ""),
    ASE256("ASE-256", "");

    String value;
    String description;

    EncryptionTypeEnum(String value, String description) {
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
