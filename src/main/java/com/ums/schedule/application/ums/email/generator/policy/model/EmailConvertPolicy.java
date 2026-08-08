package com.ums.schedule.application.ums.email.generator.policy.model;

import com.ums.schedule.application.ums.email.generator.model.RenderedTemplateContent;
import com.ums.schedule.application.ums.common.template.loader.model.EmailTemplate;
import com.ums.schedule.common.code.email.ConvertType;

import java.util.List;


public record EmailConvertPolicy(
        ConvertType convertType,
        String bodyTemplate,
        List<RenderedTemplateContent> attachments
) {
    public static EmailConvertPolicy of(ConvertType convertType, String bodyTemplate, List<RenderedTemplateContent> attachments) {
        return new EmailConvertPolicy(
                convertType,
                bodyTemplate,
                attachments
        );
    }
}
