package com.ums.schedule.template.domain;

import com.ums.schedule.common.code.EnumMapperValue;
import com.ums.schedule.template.domain.code.TemplateTypeEnum;
import com.ums.schedule.template.exception.TemplateContentRequiredException;
import org.springframework.util.StringUtils;

import static com.ums.schedule.template.domain.code.TemplateTypeEnum.valueOf;

public record TemplateTypeContent(
        TemplateTypeEnum templateType,
        String prefix,
        String content
) {
    public static TemplateTypeContent ofWithPrefix(EnumMapperValue templateType, String prefix, String content) {
        return new TemplateTypeContent(
                valueOf(templateType.value()),
                prefix,
                content
        );
    }

    public static TemplateTypeContent ofWithoutPrefix(EnumMapperValue templateType, String content) {
        return new TemplateTypeContent(
                valueOf(templateType.value()),null, content);
    }

    public String toWithPrefix() {
        return this.prefix.concat(this.content);
    }

    private void validate() {
        if(!StringUtils.hasText(this.content)) {
            throw TemplateContentRequiredException.ofTitle();
        }
    }
}
