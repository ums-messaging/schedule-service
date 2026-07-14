package com.ums.schedule.fixture.email.attachment;

import com.ums.schedule.common.code.email.ConvertTypeEnum;
import com.ums.schedule.domain.message.email.attachment.EmailAttachment;
import com.ums.schedule.domain.message.email.SecurityMailPolicy;
import com.ums.schedule.domain.message.email.EmailSendMessage;
import com.ums.schedule.domain.sendrequest.SendRequest;

public class EmailAttachmentBuilder {
    private Long id;
    private ConvertTypeEnum convertType;
    private SecurityMailPolicy securityPolicy;
    private String attachmentName;
    private String downloadName;
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


    public EmailAttachmentBuilder fileKeyTemplate(String fileKeyTemplate) {
        this.fileKeyTemplate = fileKeyTemplate;
        return this;
    }

    public EmailAttachmentBuilder securityPolicy(SecurityMailPolicy securityPolicy) {
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
                attachmentName,
                downloadName,
                fileKeyTemplate,
                fileKey,
                fileSize,
                sendMessage
        );
    }
}
