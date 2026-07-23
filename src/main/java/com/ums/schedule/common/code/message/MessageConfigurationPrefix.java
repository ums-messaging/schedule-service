package com.ums.schedule.common.code.message;

import com.ums.schedule.common.code.mapper.EnumMapperType;

public enum MessageConfigurationPrefix implements EnumMapperType {
    ADVERTISE_PREFIX("message.advertise.prefix", "광고문구");

    String value;
    String description;

    MessageConfigurationPrefix(String value, String description) {
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
