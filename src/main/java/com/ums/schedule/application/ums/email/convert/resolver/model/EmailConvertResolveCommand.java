package com.ums.schedule.application.ums.email.convert.resolver.model;

import com.ums.schedule.adapter.api.request.email.EmailSendCreateRequest;
import com.ums.schedule.application.ums.email.attachment.model.AttachmentContext;
import com.ums.schedule.application.ums.email.convert.strategy.model.EmailConvertPolicyContext;
import com.ums.schedule.application.ums.email.security.SecurityMail;
import com.ums.schedule.application.ums.email.template.query.model.EmailTemplateContentResult;
import com.ums.schedule.application.ums.email.template.query.model.EmailTemplateDetailResult;
import com.ums.schedule.application.ums.email.template.query.model.EmailTemplateResult;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.domain.message.email.code.AttachmentType;
import com.ums.schedule.domain.sendrequest.template.email.code.EmailTemplateSectionEnum;

import java.util.List;
import java.util.Optional;

public record EmailConvertResolveCommand(
        String convertType,
        AttachmentContext body,
        AttachmentContext cover
) {

    public static EmailConvertResolveCommand of(String convertType, AttachmentContext body, EmailTemplateResult template) {
        return new EmailConvertResolveCommand(
                convertType,
                body,
                template.coverTemplate()
        );
    }

    public EmailConvertPolicyContext toPolicyCommand(EnumMapperValue convertType, SecurityMail securityMail) {
        return EmailConvertPolicyContext.of(convertType, this, securityMail);
    }

    public String coverKey() {
        return Optional.ofNullable(this.cover)
                .map(AttachmentContext::key)
                .orElse(null);
    }
}
