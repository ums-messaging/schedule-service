package com.ums.schedule.send.code;

import com.ums.schedule.common.code.EnumMapperType;

public enum TargetColumnEnum implements EnumMapperType {
    TARGET_KEY("id", "대상자 KEY"),
    TARGET_NAME("name", "이름"),
    TARGET_EMAIL("email","이메일 주소"),
    TARGET_PHONE("phone", "핸드폰 번호"),
    TARGET_BIRTHDAY("birthday", "생년월일");

    String value;
    String description;

    TargetColumnEnum(String value, String description) {
        this.value = value;
        this.description = description;
    }

    @Override
    public String code() {
        return this.name();
    }

    @Override
    public String value() {
        return this.value();
    }

    @Override
    public String description() {
        return this.description();
    }
}
