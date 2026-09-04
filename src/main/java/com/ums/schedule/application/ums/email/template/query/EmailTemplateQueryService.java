package com.ums.schedule.application.ums.email.template.query;

import com.ums.schedule.application.ums.email.template.query.model.EmailTemplateDetailQuery;
import com.ums.schedule.application.ums.email.template.query.model.EmailTemplateResult;

public interface EmailTemplateQueryService {
    EmailTemplateResult findTemplate(EmailTemplateDetailQuery command);
}
