package com.ums.schedule.common.code.schedule;

import com.ums.schedule.common.code.mapper.EnumMapperType;

public enum CycleCd implements EnumMapperType {
    ONCE("ONCE", "한번만"),
    ALWAYS("ALWAYS","항상"),
    MONTH("MONTH","월 주기"),
    DAY("DAY","일 주기"),
    HOUR("HOUR", "시간 주기"),
    MINUTE("MINUTE", "분 주기");

    String value;
    String description;

    CycleCd(String value, String description) {
        this.value = value;
        this.description = description;
    }

    @Override
    public String code() {
        return null;
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
