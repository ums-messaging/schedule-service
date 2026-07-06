package com.ums.schedule.domain.sendrequest.resource.email;

import com.ums.schedule.domain.sendrequest.resource.email.policy.AttachmentPolicy;
import com.ums.schedule.domain.sendrequest.resource.email.code.ConvertTypeEnum;
import com.ums.schedule.domain.sendrequest.resource.email.policy.SecurityPolicy;
import com.ums.schedule.domain.sendrequest.message.email.EmailSendMessage;
import com.ums.schedule.domain.sendrequest.SendRequest;

public class EmailAttachmentBuilder {
    private Long id;
    private ConvertTypeEnum convertType;
    private SecurityPolicy securityPolicy;
    private AttachmentPolicy attachmentPolicy;
    private String fileKeyTemplate;
    private String fileKey;
    private Long fileSize;
    private EmailSendMessage sendMessage;

    public EmailAttachmentBuilder() {
        this.convertType = ConvertTypeEnum.NONE;
        this.securityPolicy = null;
    }

    public static EmailAttachmentBuilder builder() {
        return new EmailAttachmentBuilder();
    }

    public EmailAttachmentBuilder convertType(ConvertTypeEnum convertType) {
        this.convertType = convertType;
        return this;
    }

    public EmailAttachmentBuilder attachmentPolicy(String attachmentName, String downloadName) {
        this.attachmentPolicy = AttachmentPolicy.of(attachmentName, downloadName);
        return this;
    }

    public EmailAttachmentBuilder fileKeyTemplate(String fileKeyTemplate) {
        this.fileKeyTemplate = fileKeyTemplate;
        return this;
    }

    public EmailAttachmentBuilder securityPolicy(SecurityPolicy securityPolicy) {
        this.securityPolicy = securityPolicy;
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



    public EmailAttachmentBuilder sendRequest(SendRequest sendRequest) {
        return this;
    }

    public EmailAttachment build() {

        return new EmailAttachment(
                id,
                convertType,
                securityPolicy,
                attachmentPolicy,
                fileKeyTemplate,
                fileKey,
                fileSize,
                sendMessage
        );
    }
}
