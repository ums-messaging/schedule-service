package com.ums.schedule.application.sendrequest.template.email;

import com.ums.schedule.adapter.api.request.email.EmailSendCreateRequest;
import com.ums.schedule.application.sendrequest.message.email.result.EmailTemplateResult;

public interface EmailTemplateService {
    EmailTemplateResult findTemplate(String templateKey, EmailSendCreateRequest request);
}
