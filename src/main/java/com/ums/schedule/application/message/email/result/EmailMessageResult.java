package com.ums.schedule.application.message.email.result;

import com.ums.schedule.adapter.api.request.email.EmailSecurityPolicyRequest;
import com.ums.schedule.adapter.api.request.email.EmailAttachmentRequest;
import com.ums.schedule.application.message.email.model.AttachmentCreateCommand;
import com.ums.schedule.common.code.email.ConvertType;
import com.ums.schedule.domain.message.email.attachment.EmailAttachment;
import com.ums.schedule.domain.message.email.EmailSendMessage;
import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.domain.request.target.upload.TargetUploadReport;
import com.ums.schedule.common.code.target_upload.TargetUploadTypeEnum;

import java.util.List;

public record EmailMessageResult(
        String messageId,
        ConvertType convertType,
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
                ConvertType.valueOf(body.convertType().code()),
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
