package com.ums.schedule.application.message.email.result;

import com.ums.schedule.adapter.api.request.email.EmailSecurityPolicyRequest;
import com.ums.schedule.adapter.api.request.email.EmailAttachmentRequest;
import com.ums.schedule.application.message.email.model.AttachmentCreateCommand;
import com.ums.schedule.domain.sendrequest.resource.email.EmailAttachment;
import com.ums.schedule.domain.sendrequest.resource.email.code.ConvertTypeEnum;
import com.ums.schedule.domain.sendrequest.message.email.EmailSendMessage;
import com.ums.schedule.domain.sendrequest.SendRequest;
import com.ums.schedule.domain.sendrequest.target.upload.TargetUploadReport;
import com.ums.schedule.domain.sendrequest.target.upload.code.TargetUploadTypeEnum;

import java.util.List;

public record EmailMessageResult(
        String messageId,
        ConvertTypeEnum convertType,
        TargetUploadTypeEnum uploadType,
        String templateKey,
        String title,
        String content,
        EmailSecurityPolicyRequest securityPolicy,
        List<EmailAttachmentRequest> attachments
) {
    public static EmailMessageResult of(EmailSendMessage sendMessage, AttachmentCreateCommand body, List<EmailAttachment> messages) {
        SendRequest sendRequest = sendMessage.getSendMessage().getSendRequest();
        TargetUploadReport targetUpload = sendRequest.getCurrentTargetUpload();
        return new EmailMessageResult(
                sendMessage.getId().toString(),
                ConvertTypeEnum.valueOf(body.convertType().code()),
                targetUpload.getUploadType(),
                sendRequest.getTemplateKey(),
                sendMessage.getSubject(),
                sendMessage.getBodyTemplate(),
                messages.stream()
                        .filter(msg -> msg.getSecurityPolicy() != null)
                        .map(msg -> EmailSecurityPolicyRequest.of(msg.getSecurityPolicy()))
                        .findAny()
                        .orElse(null),
                messages.stream()
                        .map(msg -> EmailAttachmentRequest.of(msg))
                        .toList()
        );
    }
}
