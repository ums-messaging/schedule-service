package com.ums.schedule.template.infrastructure;

import com.ums.schedule.template.application.response.TemplateResponse;

public interface TemplateClient {
    TemplateResponse getTemplate(String templateId);
}
