package com.ums.schedule.common.code.email;


import com.ums.schedule.common.code.mapper.EnumMapperType;

public enum EmailTemplateSectionEnum implements EnumMapperType {
    HEADER("header", "이메일 상단"),
    BODY("body", "메시지 본문"),
    FOOTER("footer", "이메일 하단"),
    COVER("cover", "이메일 커버"),
    ATTACHMENT("attachment", "첨부파일");

    String value;
    String description;

    EmailTemplateSectionEnum(String value, String description) {
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
