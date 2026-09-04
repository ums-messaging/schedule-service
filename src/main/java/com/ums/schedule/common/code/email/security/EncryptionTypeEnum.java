package com.ums.schedule.common.code.email.security;


import com.ums.schedule.common.code.mapper.EnumMapperType;

public enum EncryptionTypeEnum implements EnumMapperType {
    ASE128("ASE-128", "128"),
    ASE256("ASE-256", "256");

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

    public int length() {
        return Integer.parseInt(description);
    }
}
