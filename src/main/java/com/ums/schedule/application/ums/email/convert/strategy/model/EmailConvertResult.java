package com.ums.schedule.application.ums.email.convert.strategy.model;

import com.ums.schedule.application.ums.email.attachment.model.AttachmentCreateCommand;
import com.ums.schedule.application.ums.email.convert.ConvertedAttachment;
import com.ums.schedule.application.ums.email.security.SecurityMail;
import com.ums.schedule.domain.message.email.SecurityMailPolicy;

import java.util.Optional;

public record EmailConvertResult(
        String bodyKey,
        SecurityMailPolicy securityMail,
        ConvertedAttachment convertedAttachment
) {
    public static EmailConvertResult of(String bodyKey, SecurityMailPolicy securityMail, ConvertedAttachment convertedAttachment) {
        return new EmailConvertResult(bodyKey, securityMail, convertedAttachment);
    }

    public static EmailConvertResult of(String bodyKey) {
        return new EmailConvertResult(bodyKey, null,null);
    }

}
