package com.ums.schedule.template.application.resolver;

import com.ums.schedule.common.code.EnumMapperSelector;
import com.ums.schedule.template.application.response.email.EmailContentResponse;
import freemarker.template.Template;

import java.io.IOException;

public interface TemplateFormatResolver extends EnumMapperSelector {
    Template loadTemplate(EmailContentResponse content) throws IOException;
}
