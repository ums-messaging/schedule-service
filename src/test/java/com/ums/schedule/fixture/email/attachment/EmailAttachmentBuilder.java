package com.ums.schedule.fixture.email.attachment;

import com.ums.schedule.common.code.email.AttachmentType;
import com.ums.schedule.common.code.email.ConvertType;
import com.ums.schedule.domain.message.email.attachment.EmailAttachment;
import com.ums.schedule.domain.message.email.security.SecurityMailPolicy;
import com.ums.schedule.domain.message.email.EmailSendMessage;
import com.ums.schedule.domain.request.SendRequest;

import java.util.List;
import java.util.UUID;

public class EmailAttachmentBuilder {
    private UUID id;
    private AttachmentType type;
    private String attachmentName;
    private String downloadName;
    private String fileKey;
    private Long fileSize;
    private EmailSendMessage sendMessage;

    public EmailAttachmentBuilder() {
        this.type = AttachmentType.DIRECT;
        this.fileKey = "document.pdf";
        this.attachmentName = "8 month bills.";
        this.downloadName = "guest's bills.";
    }

    public static EmailAttachmentBuilder builder() {
        return new EmailAttachmentBuilder();
    }


    public EmailAttachmentBuilder attachmentType(AttachmentType type) {
        this.type = type;
        return this;
    }

    public EmailAttachmentBuilder fileKey(String fileKey) {
        this.fileKey = fileKey;
        return this;
    }

    public EmailAttachmentBuilder fileSize(Long fileSize) {
        this.fileSize = fileSize;
        return this;
    }

    public EmailAttachmentBuilder attachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
        return this;
    }

    public EmailAttachmentBuilder downloadName(String downloadName) {
        this.downloadName = downloadName;
        return this;
    }

    public EmailAttachmentBuilder sendMessage(EmailSendMessage sendMessage) {
        this.sendMessage = sendMessage;
        return this;
    }

    public EmailAttachment build() {

        return new EmailAttachment(
                id,
                type,
                attachmentName,
                downloadName,
                fileKey,
                fileSize,
                sendMessage
        );
    }
}
