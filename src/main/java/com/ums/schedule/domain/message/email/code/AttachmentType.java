package com.ums.schedule.domain.message.email.code;

import com.ums.schedule.common.code.mapper.EnumMapperType;

public enum AttachmentType implements EnumMapperType {
    DIRECT("fileKey", ""),
    TEMPLATE("fileKeyTemplate", "")
    ;

    String value;
    String description;

    AttachmentType(String value, String description) {
        this.value = value;
        this.description = description;
    }

    @Override
    public String code() {
        return this.name();
    }

    @Override
    public String value() {
        return null;
    }

    @Override
    public String description() {
        return null;
    }
}
