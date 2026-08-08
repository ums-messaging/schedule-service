package com.ums.schedule.application.ums.email.request.model;

import com.ums.schedule.application.target.report.model.TargetUploadResult;
import com.ums.schedule.application.ums.common.request.model.SendRequestCreateResult;
import com.ums.schedule.application.ums.common.request.model.TargetUploadCreateSummary;
import com.ums.schedule.common.code.email.ConvertType;
import com.ums.schedule.domain.message.email.EmailSendMessage;

import java.util.Optional;

public record EmailSendRequestCreateSummary(
        Long requestId,
        Integer retryCount,
        String messageId,
        ConvertType convertType,
        Integer attachmentCount,
        TargetUploadCreateSummary targetUploadSummary
) {
    public static EmailSendRequestCreateSummary of(EmailSendMessage sendMessage, SendRequestCreateResult result) {
        TargetUploadCreateSummary summary = Optional.ofNullable(result.targetUploadReport())
                .map(TargetUploadResult::toSummary)
                .orElseGet(() -> null);
        return new EmailSendRequestCreateSummary(
                result.requestId(),
                result.retryCount(),
                result.messageId(),
                sendMessage.getConvertType(),
                sendMessage.getAttachmentCount(),
                summary
        );
    }
}
