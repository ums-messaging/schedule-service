package com.ums.schedule.template.domain.code;

import com.ums.schedule.common.code.EnumMapperType;

public enum EmailTemplateSectionEnum implements EnumMapperType {
    HEADER("HEADER", "이메일 상단"),
    BODY("BODY", "메시지 본문"),
    FOOTER("FOOTER", "이메일 하단"),
    COVER("COVER", "이메일 커버"),
    ATTACHMENT("ATTACHMENT", "첨부파일");

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
        return this.description();
    }
}
