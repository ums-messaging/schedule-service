package com.ums.schedule.application.ums.email.attachment.model;

import com.ums.schedule.application.ums.email.convert.ConvertedAttachment;
import com.ums.schedule.domain.message.email.SecurityMailPolicy;
import com.ums.schedule.domain.message.email.code.AttachmentType;
import com.ums.schedule.domain.message.email.code.ConvertTypeEnum;
import com.ums.schedule.domain.message.exception.EmailSendMessage;

import java.util.Map;


public record AttachmentCreateCommand(
        EmailSendMessage sendMessage,
        ConvertTypeEnum convertType,
        SecurityMailPolicy securityMail,
        Map<AttachmentType, String> keyMap,
        String attachmentName,
        String downloadName,
        Long fileSize
        ) {
    public static AttachmentCreateCommand of(EmailSendMessage sendMessage, SecurityMailPolicy securityMail,
                                                ConvertedAttachment convertedAttachment) {
        return new AttachmentCreateCommand(
                sendMessage,
                convertedAttachment.convertType(),
                securityMail,
                convertedAttachment.toKeyMap(),
                convertedAttachment.attachmentName(),
                convertedAttachment.downloadName(),
                convertedAttachment.fileSize()
        );
    }
    public static AttachmentCreateCommand of(EmailSendMessage sendMessage, ConvertedAttachment attachment) {
        return new AttachmentCreateCommand(
                sendMessage,
                ConvertTypeEnum.NONE,
                null,
                attachment.toKeyMap(),
                attachment.attachmentName(),
                attachment.downloadName(),
                attachment.fileSize()
        );
    }
}
