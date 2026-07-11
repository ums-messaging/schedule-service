package com.ums.schedule.fixture.email.convert;

import com.ums.schedule.application.ums.email.convert.ConvertedAttachment;
import com.ums.schedule.application.ums.email.convert.strategy.model.EmailConvertResult;

import java.util.List;

public class EmailConvertResultBuilder {
    private String bodyKey;
    private List<ConvertedAttachment> convertedAttachments;

    public static EmailConvertResultBuilder builder() {
        return new EmailConvertResultBuilder();
    }

    public EmailConvertResultBuilder bodyKey(String bodyKey) {
        this.bodyKey = bodyKey;
        return this;
    }

    public EmailConvertResultBuilder convertedAttachments(List<ConvertedAttachment> attachments) {
        this.convertedAttachments = attachments;
        return this;
    }

    public EmailConvertResult build() {
        return new EmailConvertResult(
                this.bodyKey,
                this.convertedAttachments
        );
    }
}
