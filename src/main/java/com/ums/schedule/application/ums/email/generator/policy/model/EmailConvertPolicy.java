package com.ums.schedule.application.ums.email.generator.policy.model;

import com.ums.schedule.common.code.email.ConvertType;
import com.ums.schedule.domain.target.message.AttachmentPayload;
import freemarker.template.Template;

import java.util.List;


public record EmailConvertPolicy(
        ConvertType convertType,
        Template bodyTemplate,
        List<AttachmentPayload> attachments
) {
    public static EmailConvertPolicy of(ConvertType convertType, Template bodyTemplate, List<AttachmentPayload> attachments) {
        return new EmailConvertPolicy(
                convertType,
                bodyTemplate,
                attachments
        );
    }
}
