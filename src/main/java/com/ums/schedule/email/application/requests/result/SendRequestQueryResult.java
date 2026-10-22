package com.ums.schedule.email.application.requests.result;

import com.ums.schedule.email.domain.attachment.EmailAttachment;
import com.ums.schedule.email.domain.message.EmailSendMessage;
import com.ums.schedule.email.domain.requests.EmailSendRequest;
import com.ums.schedule.email.domain.template.EmailTemplate;

import java.util.List;

public record SendRequestQueryResult (
        EmailSendRequest sendRequest,
        EmailSendMessage message,
        EmailTemplate template,
        List<EmailAttachment> attachmentList
) {
    public static SendRequestQueryResult of(EmailSendRequest sendRequest, EmailSendMessage message, EmailTemplate template, List<EmailAttachment> attachments) {
        return new SendRequestQueryResult(sendRequest, message, template, attachments);
    }
}
