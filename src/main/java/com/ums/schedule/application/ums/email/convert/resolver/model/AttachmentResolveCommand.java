package com.ums.schedule.application.ums.email.convert.resolver.model;

import com.ums.schedule.application.ums.email.template.query.model.EmailTemplateContentResult;

public record AttachmentResolveCommand(
        String fileKey,
        String fileKeyTemplate
) {
    public static AttachmentResolveCommand of(EmailTemplateContentResult content) {
        return new AttachmentResolveCommand(content.fileKey(), content.fileKeyTemplate());
    }
}
