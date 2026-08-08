package com.ums.schedule.fixture.email.convert;

import com.ums.schedule.application.ums.email.message.provider.EmailPolicyResult;
import com.ums.schedule.application.ums.email.security.SecurityMail;
import com.ums.schedule.domain.message.email.convert.ConvertMail;

public class EmailPolicyResultBuilder {
    private SecurityMail securityMail;
    private ConvertMail convertMail;

    public static EmailPolicyResultBuilder builder() {
        return new EmailPolicyResultBuilder();
    }

    public EmailPolicyResultBuilder securityMail(SecurityMail securityMail) {
        this.securityMail = securityMail;
        return this;
    }

    public EmailPolicyResultBuilder convertMail(ConvertMail convertMail) {
        this.convertMail = convertMail;
        return this;
    }

    public EmailPolicyResult build() {
        return new EmailPolicyResult(
                securityMail,
                convertMail
        );
    }
}
