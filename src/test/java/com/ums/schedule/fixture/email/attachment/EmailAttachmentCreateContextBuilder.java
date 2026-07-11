package com.ums.schedule.fixture.email.attachment;

import com.ums.schedule.application.ums.email.attachment.model.EmailAttachmentCreateContext;
import com.ums.schedule.application.ums.email.convert.ConvertedAttachment;
import com.ums.schedule.domain.message.exception.EmailSendMessage;
import com.ums.schedule.domain.message.email.SecurityMailPolicy;

public class EmailAttachmentCreateContextBuilder {
    private EmailSendMessage sendMessage;
    private SecurityMailPolicy securityMail;
    private ConvertedAttachment convertedAttachment;
    private String attachmentName;
    private String downloadName;
    private String fileKey;
    private Long fileSize;

    public static EmailAttachmentCreateContextBuilder builder() {
        return new EmailAttachmentCreateContextBuilder();
    }

    private EmailAttachmentCreateContextBuilder() {
        this.attachmentName = "첨부파일명.pdf";
        this.downloadName = "다운로드명.pdf";
        this.fileKey = "body.html";
        this.fileSize = 10L;
    }


    public EmailAttachmentCreateContextBuilder sendMessage(EmailSendMessage sendMessage) {
        this.sendMessage = sendMessage;
        return this;
    }

    public EmailAttachmentCreateContextBuilder securityMailPolicy(SecurityMailPolicy securityMail) {
        this.securityMail = securityMail;
        return this;
    }

    public EmailAttachmentCreateContextBuilder convertedAttachment(ConvertedAttachment attachment) {
        this.convertedAttachment = attachment;
        return this;
    }

    public EmailAttachmentCreateContextBuilder attachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
        return this;
    }

    public EmailAttachmentCreateContextBuilder downloadName(String downloadName) {
        this.downloadName = downloadName;
        return this;
    }

    public EmailAttachmentCreateContextBuilder fileKey(String fileKey) {
        this.fileKey = fileKey;
        return this;
    }

    public EmailAttachmentCreateContextBuilder fileSize(Long fileSize) {
        this.fileSize = fileSize;
        return this;
    }

    public EmailAttachmentCreateContext build() {
        return new EmailAttachmentCreateContext(
                sendMessage,
                securityMail,
                convertedAttachment,
                attachmentName,
                downloadName,
                fileKey,
                fileSize
        );
    }

}
