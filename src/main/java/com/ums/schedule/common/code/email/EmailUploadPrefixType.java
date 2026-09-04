package com.ums.schedule.common.code.email;

import com.ums.schedule.common.code.mapper.EnumMapperType;

public enum EmailUploadPrefixType implements EnumMapperType {
    TEMPLATE_PREFIX("template_prefix", "템플릿 기본 경로"),
    IMAGE_SUFFIX("image_suffix", "이미지 경로"),
    ATTACHMENT_SUFFIX("attachment_suffix", "첨부파일 경로");

    String value;
    String description;

    EmailUploadPrefixType(String value, String description) {
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
