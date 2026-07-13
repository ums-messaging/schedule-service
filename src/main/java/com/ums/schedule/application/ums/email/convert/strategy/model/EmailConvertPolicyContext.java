package com.ums.schedule.application.ums.email.convert.strategy.model;

import com.ums.schedule.application.ums.email.attachment.model.AttachmentContext;
import com.ums.schedule.application.ums.email.convert.resolver.model.EmailConvertResolveCommand;
import com.ums.schedule.application.ums.email.security.SecurityMail;
import com.ums.schedule.common.code.mapper.EnumMapperValue;


public record EmailConvertPolicyContext(
        EnumMapperValue convertType,
        SecurityMail securityMail,
        AttachmentContext body,
        String coverKey
        ) {

    public static EmailConvertPolicyContext of(EnumMapperValue convertType, EmailConvertResolveCommand command, SecurityMail securityMail) {
        return new EmailConvertPolicyContext(
                convertType,
                securityMail,
                command.body(),
                command.coverKey()
        );
    }
}
