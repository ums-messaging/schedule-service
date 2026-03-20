package com.ums.schedule.template.application.resolver;

import com.ums.schedule.common.code.EnumMapperSelector;
import com.ums.schedule.template.application.response.email.EmailContentResponse;
import com.ums.schedule.template.domain.email.EmailContent;

public interface TemplateFormatResolver extends EnumMapperSelector {
    EmailContent loadTemplate(EmailContentResponse content);
}
