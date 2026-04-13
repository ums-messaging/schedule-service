package com.ums.schedule.adapter.api.template.email;

import com.ums.schedule.adapter.api.template.email.EmailTemplateResponse;

public interface EmailTemplateClient {
    EmailTemplateResponse getTemplate(String templateId);
}
