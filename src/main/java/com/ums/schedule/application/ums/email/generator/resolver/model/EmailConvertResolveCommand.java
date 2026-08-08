package com.ums.schedule.application.ums.email.generator.resolver.model;

import com.ums.schedule.application.ums.email.attachment.model.AttachmentContext;
import com.ums.schedule.application.ums.email.template.query.model.EmailTemplateResult;

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


    public String coverKey() {
        return Optional.ofNullable(this.cover)
                .map(AttachmentContext::key)
                .orElse(null);
    }
}
