package com.ums.schedule.send.code;

import com.ums.schedule.common.code.EnumMapperType;

public enum TargetErrorEnum implements EnumMapperType  {
    INVALID_EMAIL("", "올바른 형식의 이메일이 아님"),
    MESSAGE_PARSING_ERROR("", "메시지 PARSING 실패"),

    ETC("", "기타")
    ;

    String value;
    String description;

    TargetErrorEnum(String value, String description) {
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
