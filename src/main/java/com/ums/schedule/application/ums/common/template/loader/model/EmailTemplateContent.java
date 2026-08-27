package com.ums.schedule.application.ums.common.template.loader.model;

import com.ums.schedule.application.sendrequest.target.data.TargetMessageData;
import com.ums.schedule.common.code.email.ConvertType;
import com.ums.schedule.domain.message.email.attachment.EmailAttachment;
import com.ums.schedule.domain.message.email.convert.ConvertMail;
import com.ums.schedule.domain.target.TargetMessage;
import com.ums.schedule.domain.target.message.AttachmentPayload;
import com.ums.schedule.domain.target.message.EmailTargetMessage;
import freemarker.template.Template;

public record EmailTemplateContent(
        Template template,
        String fileKey,
        String fileKeyTemplate,
        String attachmentName,
        String downloadName
) {
    public static EmailTemplateContent of(Template template) {
        return new EmailTemplateContent(
                template,
                template.getName(),
                null,
                null,
                null
        );
    }

    public  static EmailTemplateContent of(Template template, ConvertMail convertMail) {
        return new EmailTemplateContent(
                template,
                template.getName(),
                convertMail.getFileKeyTemplate(),
                convertMail.getAttachmentName(),
                convertMail.getDownloadName()
        );
    }

    public static EmailTemplateContent of(EmailAttachment attachment) {
        return new EmailTemplateContent(
                null,
                attachment.fileKey(),
                attachment.fileKeyTemplate(),
                attachment.getAttachmentName(),
                attachment.getDownloadName()
        );
    }

}
