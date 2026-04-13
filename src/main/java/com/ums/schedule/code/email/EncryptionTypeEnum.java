package com.ums.schedule.code.email;


import com.ums.schedule.code.EnumMapperType;

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
}
