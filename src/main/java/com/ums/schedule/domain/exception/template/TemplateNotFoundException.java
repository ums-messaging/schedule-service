package com.ums.schedule.domain.exception.template;

import com.ums.schedule.domain.exception.ResourceNotFoundException;

public class TemplateNotFoundException extends ResourceNotFoundException {

    protected TemplateNotFoundException() {
        super("Template");
    }

    public static TemplateNotFoundException of() {
        return new TemplateNotFoundException();
    }
}
