package com.ums.schedule.domain.send.email.code;

import com.ums.schedule.common.code.mapper.EnumMapperType;

public enum EmailCacheKeyCode implements EnumMapperType {
    JOB_ID("job_id:", ""),
    GROUP_ID("group_id:", "");

    String value;
    String description;

    EmailCacheKeyCode(String value, String description) {
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
