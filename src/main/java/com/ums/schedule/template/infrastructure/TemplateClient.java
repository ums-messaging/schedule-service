package com.ums.schedule.template.infrastructure;

import com.ums.schedule.template.application.response.TemplateResponse;
import com.ums.schedule.template.application.response.email.EmailTemplateResponse;

public interface TemplateClient {
    EmailTemplateResponse getTemplate(String templateId);
}
