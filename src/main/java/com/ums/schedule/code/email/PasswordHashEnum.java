package com.ums.schedule.code.email;


import com.ums.schedule.code.EnumMapperType;

public enum PasswordHashEnum implements EnumMapperType {
    SHA256("SHA-256","SHA-256");
    String value;
    String description;

    PasswordHashEnum(String value, String description) {
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
