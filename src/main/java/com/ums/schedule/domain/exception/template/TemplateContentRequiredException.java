package com.ums.schedule.domain.exception.template;

import com.ums.schedule.common.code.email.EmailMessageSection;

public class TemplateContentRequiredException extends TemplateException {
    protected TemplateContentRequiredException(String message) {
        super(message);
    }

    public static TemplateContentRequiredException ofBody() {
        return new TemplateContentRequiredException(EmailMessageSection.BODY.value());
    }

    public static TemplateContentRequiredException ofCover() {
        return new TemplateContentRequiredException(EmailMessageSection.COVER.value());
    }

    public static TemplateContentRequiredException ofImageDir() {
        return new TemplateContentRequiredException("Image dir ");
    }

    public static TemplateContentRequiredException ofTemplateKey() {
        return new TemplateContentRequiredException("Template key ");
    }

    public static TemplateContentRequiredException ofTitle() {
        return new TemplateContentRequiredException("Template title ");
    }
}
