package com.ums.schedule.fixture.email.convert;

import com.ums.schedule.application.ums.email.convert.ConvertedAttachment;
import com.ums.schedule.application.ums.email.convert.strategy.model.EmailConvertResult;
import com.ums.schedule.domain.message.email.SecurityMailPolicy;

import java.util.List;

public class EmailConvertResultBuilder {
    private String bodyKey;
    private SecurityMailPolicy securityMail;
    private ConvertedAttachment convertedAttachment;

    public static EmailConvertResultBuilder builder() {
        return new EmailConvertResultBuilder();
    }

    public EmailConvertResultBuilder bodyKey(String bodyKey) {
        this.bodyKey = bodyKey;
        return this;
    }

    public EmailConvertResultBuilder securityMail(SecurityMailPolicy securityMail) {
        this.securityMail = securityMail;
        return this;
    }

    public EmailConvertResultBuilder convertedAttachment(ConvertedAttachment convertedAttachment) {
        this.convertedAttachment = convertedAttachment;
        return this;
    }

    public EmailConvertResult build() {
        return new EmailConvertResult(
                this.bodyKey,
                this.securityMail,
                this.convertedAttachment
        );
    }
}
