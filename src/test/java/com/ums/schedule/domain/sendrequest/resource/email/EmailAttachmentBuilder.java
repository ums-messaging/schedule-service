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
