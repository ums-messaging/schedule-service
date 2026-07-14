package com.ums.schedule.fixture.email.attachment;

import com.ums.schedule.application.ums.email.attachment.model.AttachmentCreateCommand;
import com.ums.schedule.domain.message.email.code.AttachmentType;
import com.ums.schedule.domain.message.email.code.ConvertTypeEnum;
import com.ums.schedule.domain.message.email.EmailSendMessage;
import com.ums.schedule.domain.message.email.SecurityMailPolicy;

import java.util.Map;

public class EmailAttachmentCreateContextBuilder {
    private EmailSendMessage sendMessage;
    private ConvertTypeEnum convertType;
    private SecurityMailPolicy securityMail;
    private Map<AttachmentType, String> keyMap;
    private String attachmentName;
    private String downloadName;
    private Long fileSize;

    public static EmailAttachmentCreateContextBuilder builder() {
        return new EmailAttachmentCreateContextBuilder();
    }

    private EmailAttachmentCreateContextBuilder() {
        this.attachmentName = "첨부파일명.pdf";
        this.downloadName = "다운로드명.pdf";
        this.fileSize = 10L;
    }


    public EmailAttachmentCreateContextBuilder sendMessage(EmailSendMessage sendMessage) {
        this.sendMessage = sendMessage;
        return this;
    }

    public EmailAttachmentCreateContextBuilder convertType(ConvertTypeEnum convertType) {
        this.convertType = convertType;
        return this;
    }

    public EmailAttachmentCreateContextBuilder securityMail(SecurityMailPolicy securityMail) {
        this.securityMail = securityMail;
        return this;
    }

    public EmailAttachmentCreateContextBuilder fileKeyMap(Map<AttachmentType, String> fileKeyMap) {
        this.keyMap = fileKeyMap;
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


    public EmailAttachmentCreateContextBuilder fileSize(Long fileSize) {
        this.fileSize = fileSize;
        return this;
    }

    public AttachmentCreateCommand build() {
        return new AttachmentCreateCommand(
                sendMessage,
                convertType,
                securityMail,
                keyMap,
                attachmentName,
                downloadName,
                fileSize
        );
    }

}
