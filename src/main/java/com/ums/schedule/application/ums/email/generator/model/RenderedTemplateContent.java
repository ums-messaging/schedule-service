package com.ums.schedule.application.ums.email.generator.model;

import com.ums.schedule.application.sendrequest.target.data.TargetMessageData;
import com.ums.schedule.application.ums.common.template.loader.model.EmailTemplateContent;
import org.springframework.util.StringUtils;

public record RenderedTemplateContent(
        String fileKey,
        String template,
        String attachmentName,
        String downloadName
) {
    public static RenderedTemplateContent of(EmailTemplateContent content, String template, TargetMessageData targetMessageData) {
        return new RenderedTemplateContent(
                targetMessageData.parse(content.fileKeyTemplate()),
                template,
                targetMessageData.parse(content.attachmentName()),
                targetMessageData.parse(content.downloadName())
        );
    }

    public static RenderedTemplateContent of(EmailTemplateContent content, TargetMessageData targetMessageData) {
        String fileKey = StringUtils.hasText(content.fileKeyTemplate()) ?
                targetMessageData.parse(content.fileKeyTemplate()) : content.fileKey();
        return new RenderedTemplateContent(
                fileKey,
                null,
                targetMessageData.parse(content.attachmentName()),
                targetMessageData.parse(content.downloadName())
        );
    }
}
