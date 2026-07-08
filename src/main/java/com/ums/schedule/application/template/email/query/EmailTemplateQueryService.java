package com.ums.schedule.application.template.email.query;

import com.ums.schedule.application.template.email.query.model.EmailTemplateDetailQuery;
import com.ums.schedule.application.template.email.query.model.EmailTemplateResult;

public interface EmailTemplateQueryService {
    EmailTemplateResult findTemplate(EmailTemplateDetailQuery command);
}
