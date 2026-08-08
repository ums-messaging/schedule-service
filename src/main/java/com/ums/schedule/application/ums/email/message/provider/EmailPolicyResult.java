package com.ums.schedule.application.ums.email.message.provider;

import com.ums.schedule.application.ums.email.security.SecurityMail;
import com.ums.schedule.domain.message.email.convert.ConvertMail;


public record EmailPolicyResult(
        SecurityMail securityMail,
        ConvertMail convertMail
) {

    public static EmailPolicyResult of(SecurityMail securityMail, ConvertMail convertMail) {
        return new EmailPolicyResult(
                securityMail,
                convertMail
        );
    }
}
