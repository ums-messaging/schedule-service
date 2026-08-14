package com.ums.schedule.application.ums.email.generator.model;

import com.ums.schedule.application.ums.common.template.loader.model.EmailTemplate;
import com.ums.schedule.application.ums.email.generator.policy.model.EmailConvertPolicy;
import com.ums.schedule.domain.message.email.EmailSendMessage;
import freemarker.template.Template;

import java.util.List;

public record EmailTargetMessageCreateContext(
        String subject,
        Template header,
        String bodyTemplate,
        Template footer,
        EmailSendMessage sendMessage,
        List<RenderedTemplateContent> attachmentList
) {

    public static EmailTargetMessageCreateContext of(EmailSendMessage sendMessage, EmailTemplate template, EmailConvertPolicy policy) {
        return new EmailTargetMessageCreateContext(
                template.getTitle(),
                template.getHeaderTemplate(),
                policy.bodyTemplate(),
                template.getFooterTemplate(),
                sendMessage,
                policy.attachments()
        );
    }
}
