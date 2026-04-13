package com.ums.schedule.code.send;

import com.ums.schedule.code.EnumMapperType;

public enum MessageStatusEnum implements EnumMapperType {
    WAIT("", "대기"),
    ACTIVE("", "활성화"),
    INACTIVE("", "비활성화")
    ;

    MessageStatusEnum(String value, String description) {
        this.value = value;
        this.description = description;
    }

    String value;
    String description;


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
