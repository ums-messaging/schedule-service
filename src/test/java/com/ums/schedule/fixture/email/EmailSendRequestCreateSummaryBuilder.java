package com.ums.schedule.fixture.email;

import com.ums.schedule.application.ums.common.request.model.TargetUploadCreateSummary;
import com.ums.schedule.application.ums.email.request.model.EmailSendRequestCreateSummary;
import com.ums.schedule.common.code.email.ConvertType;

public class EmailSendRequestCreateSummaryBuilder {
    private Long requestId;
    private Integer retryCount;
    private String messageId;
    private ConvertType convertType;
    private Integer attachmentCount;
    private TargetUploadCreateSummary targetUploadSummary;

    public static EmailSendRequestCreateSummaryBuilder builder() {
        return new EmailSendRequestCreateSummaryBuilder();
    }

    public EmailSendRequestCreateSummaryBuilder requestId(Long requestId) {
        this.requestId = requestId;
        return this;
    }

    public EmailSendRequestCreateSummaryBuilder retryCount(Integer retryCount) {
        this.retryCount = retryCount;
        return this;
    }

    public EmailSendRequestCreateSummaryBuilder messageId(String messageId) {
        this.messageId = messageId;
        return this;
    }

    public EmailSendRequestCreateSummaryBuilder convertType(ConvertType convertType) {
        this.convertType = convertType;
        return this;
    }

    public EmailSendRequestCreateSummaryBuilder attachmentCount(Integer attachmentCount) {
        this.attachmentCount = attachmentCount;
        return this;
    }

    public EmailSendRequestCreateSummaryBuilder targetUploadSummary(TargetUploadCreateSummary summary) {
        this.targetUploadSummary = summary;
        return this;
    }

    public EmailSendRequestCreateSummary build() {
        return new EmailSendRequestCreateSummary(
                this.requestId,
                this.retryCount,
                this.messageId,
                this.convertType,
                this.attachmentCount,
                this.targetUploadSummary
        );
    }
}
