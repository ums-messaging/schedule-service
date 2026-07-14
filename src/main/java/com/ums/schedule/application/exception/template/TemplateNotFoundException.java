package com.ums.schedule.application.exception.template;

import com.ums.schedule.application.exception.common.NotFoundException;
import com.ums.schedule.common.code.email.EmailTemplateSectionEnum;

public class TemplateNotFoundException extends NotFoundException {
    protected TemplateNotFoundException(String message) {
        super(message);
    }

    public static TemplateNotFoundException of(EmailTemplateSectionEnum section) {
        return new TemplateNotFoundException(section.description());
    }
}
