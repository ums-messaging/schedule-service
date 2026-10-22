package com.ums.schedule.email.application.message.provider;

import com.ums.schedule.common.code.email.ConvertType;
import com.ums.schedule.common.code.email.EmailType;
import com.ums.schedule.common.code.message.MessageType;
import com.ums.schedule.email.domain.common.MultiPartFile;
import com.ums.schedule.email.domain.message.ConvertMessage;
import com.ums.schedule.email.domain.message.security.SecurityMail;


public record EmailPolicyResult(
        EmailType emailType,
        MessageType messageType,
        ConvertMessage convertMessage,
        SecurityMail securityMail
        ) {

    public static EmailPolicyResult of(EmailType emailType, MessageType messageType, ConvertMessage convertMessage, SecurityMail securityMail) {
        return new EmailPolicyResult(
                emailType,
                messageType,
                convertMessage,
                securityMail
        );
    }
}
