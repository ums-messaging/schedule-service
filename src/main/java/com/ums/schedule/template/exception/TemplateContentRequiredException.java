package com.ums.schedule.template.exception;

import com.ums.schedule.template.domain.code.EmailTemplateSectionEnum;

import static com.ums.schedule.template.domain.code.EmailTemplateSectionEnum.BODY;
import static com.ums.schedule.template.domain.code.EmailTemplateSectionEnum.COVER;

public class TemplateContentRequiredException extends RequiredException {
    protected TemplateContentRequiredException(String message) {
        super(message);
    }

    public static TemplateContentRequiredException ofBody() {
        return new TemplateContentRequiredException(BODY.value());
    }

    public static TemplateContentRequiredException ofCover() {
        return new TemplateContentRequiredException(COVER.value());
    }
}
