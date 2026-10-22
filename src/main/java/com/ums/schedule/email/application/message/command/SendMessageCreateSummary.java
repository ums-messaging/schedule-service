package com.ums.schedule.email.application.message.command;

import com.ums.schedule.common.code.email.ConvertType;
import com.ums.schedule.common.code.email.EmailType;
import com.ums.schedule.email.domain.attachment.EmailAttachment;
import com.ums.schedule.email.domain.message.EmailSendMessage;

import java.util.List;
import java.util.Optional;

public record SendMessageCreateSummary(
        String messageId,
        ConvertType convertType,
        EmailType emailType,
        Integer attachmentCount
) {
    public static SendMessageCreateSummary of(EmailSendMessage sendMessage, List<EmailAttachment> attachments) {
        return new SendMessageCreateSummary(
                sendMessage.getId().toString(),
                Optional.ofNullable(sendMessage.getConvertMessage())
                        .map(v -> v.convertType())
                        .orElseGet(() -> ConvertType.NONE),
                sendMessage.getType(),
                attachments.size()
        );
    }
}
