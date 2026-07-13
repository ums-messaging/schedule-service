package com.ums.schedule.application.exception;

import com.ums.schedule.domain.sendrequest.template.email.code.EmailTemplateSectionEnum;

public class TemplateNotFoundException extends NotFoundException {
    protected TemplateNotFoundException(String message) {
        super(message);
    }

    public static TemplateNotFoundException of(EmailTemplateSectionEnum section) {
        return new TemplateNotFoundException(section.description());
    }
}
