package com.ums.schedule.application.sendrequest.template.email;

import com.ums.schedule.adapter.api.request.email.EmailSendCreateRequest;
import com.ums.schedule.adapter.api.template.EmailTemplateClient;
import com.ums.schedule.application.sendrequest.message.email.result.EmailTemplateResult;
import lombok.RequiredArgsConstructor;

//@Component
@RequiredArgsConstructor
public class EmailTemplateClientService implements EmailTemplateService {
    private final EmailTemplateClient templateClient;

    @Override
    public EmailTemplateResult findTemplate(String templateKey, EmailSendCreateRequest request) {
        return templateClient.getTemplate(templateKey);
    }
}
