package com.ums.schedule.domain.sendrequest.target.code;

import com.ums.schedule.common.code.mapper.EnumMapperType;

public enum SendTargetStatusEnum implements EnumMapperType {
    READY("R", "준비"),
    RETRYING("T", "재시도"),
    FAIL("O", "실패"),
    COMPLETED("E", "완료")
    ;

    String value;
    String description;

    SendTargetStatusEnum(String value, String description) {
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
