package com.ums.schedule.email.application.message.command;

import com.ums.schedule.common.code.email.ConvertType;
import com.ums.schedule.common.code.email.EmailType;
import com.ums.schedule.common.code.message.MessageType;
import com.ums.schedule.email.adaptor.in.rest.message.request.MessageCreateRequest;
import com.ums.schedule.email.application.message.provider.EmailPolicyResult;
import com.ums.schedule.email.domain.common.MultiPartFile;
import com.ums.schedule.email.domain.message.ConvertMessage;
import com.ums.schedule.email.domain.message.security.SecurityMail;


public record EmailSendMessageCreateCommand(
        EmailType type,
        MessageType messageType,
        SecurityMail securityMail,
        ConvertMessage convertMessage,
        String customerId,
        String templateId,
        String subject,
        String title,
        String description
) {
    public static EmailSendMessageCreateCommand of(String customerId, MessageCreateRequest request, EmailPolicyResult result) {
        return new EmailSendMessageCreateCommand(
                result.emailType(),
                result.messageType(),
                result.securityMail(),
                result.convertMessage(),
                customerId,
                request.templateId(),
                request.subject(),
                request.title(),
                request.description()
        );
    }
}
