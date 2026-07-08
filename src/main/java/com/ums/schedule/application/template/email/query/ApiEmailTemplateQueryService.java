package com.ums.schedule.application.template.email.query;

import com.ums.schedule.adapter.api.template.EmailTemplateClient;
import com.ums.schedule.application.template.email.query.model.EmailTemplateDetailQuery;
import com.ums.schedule.application.template.email.query.model.EmailTemplateResult;
import lombok.RequiredArgsConstructor;

//@Component
@RequiredArgsConstructor
public class ApiEmailTemplateQueryService implements EmailTemplateQueryService {
    private final EmailTemplateClient templateClient;

    @Override
    public EmailTemplateResult findTemplate(EmailTemplateDetailQuery command) {
        return templateClient.getTemplate(command.templateKey());
    }
}
