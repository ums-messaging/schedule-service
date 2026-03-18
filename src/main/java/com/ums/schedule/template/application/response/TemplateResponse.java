package com.ums.schedule.template.application.response;

import com.ums.schedule.template.application.response.email.EmailContentResponse;
import com.ums.schedule.template.application.response.email.EmailTemplateResponse;
import com.ums.schedule.template.domain.email.EmailContent;

import java.util.List;
import java.util.Map;

public record TemplateResponse(
        String templateId,
        String templateName,
        String templateType,
        String description,
        String channelTemplateId,
        String channelType,
        String msgTitle
) {

}
