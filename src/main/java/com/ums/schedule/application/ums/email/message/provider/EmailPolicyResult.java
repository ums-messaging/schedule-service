package com.ums.schedule.application.ums.email.message.provider;

import com.ums.schedule.application.ums.email.security.SecurityMail;
import com.ums.schedule.common.code.email.EmailType;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.domain.message.email.convert.ConvertMail;


public record EmailPolicyResult(
        EmailType emailType,
        SecurityMail securityMail,
        ConvertMail convertMail
) {

    public static EmailPolicyResult of(EnumMapperValue emailTypeValue, SecurityMail securityMail, ConvertMail convertMail) {
        return new EmailPolicyResult(
                EmailType.valueOf(emailTypeValue.code()),
                securityMail,
                convertMail
        );
    }
}
