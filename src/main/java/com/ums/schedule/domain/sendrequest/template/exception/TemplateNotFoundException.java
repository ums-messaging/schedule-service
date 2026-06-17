package com.ums.schedule.domain.sendrequest.template.exception;

import com.ums.schedule.common.exception.ResourceNotFoundException;

public class TemplateNotFoundException extends ResourceNotFoundException {

    protected TemplateNotFoundException() {
        super("Template");
    }

    public static TemplateNotFoundException of() {
        return new TemplateNotFoundException();
    }
}
