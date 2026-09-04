package com.ums.schedule.common.code.email;

import com.ums.schedule.common.code.mapper.EnumMapperType;

public enum EmailCacheKey implements EnumMapperType {
    JOB_ID("job_id:", ""),
    GROUP_ID("group_id:", "");

    String value;
    String description;

    EmailCacheKey(String value, String description) {
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
