package com.ums.schedule.template.application.resolver;

import com.ums.schedule.common.code.EnumMapperValue;
import com.ums.schedule.template.application.response.email.EmailContentResponse;
import com.ums.schedule.template.domain.email.EmailContent;

import static com.ums.schedule.template.domain.code.TemplateContentFormatEnum.*;

public class TemplateTextResolver implements TemplateFormatResolver {
    @Override
    public boolean supports(EnumMapperValue mapperValue) {
        return valueOf(mapperValue.value()) == TEXT;
    }

    @Override
    public EmailContent loadTemplate(EmailContentResponse content) {
        return EmailContent.of(content.content());
    }
}
