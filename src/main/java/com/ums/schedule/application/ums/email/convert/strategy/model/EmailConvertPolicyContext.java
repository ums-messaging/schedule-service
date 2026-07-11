package com.ums.schedule.application.ums.email.convert.strategy.model;

import com.ums.schedule.application.ums.email.convert.ConvertedAttachment;
import com.ums.schedule.application.ums.email.convert.resolver.model.EmailConvertResolveCommand;
import com.ums.schedule.common.code.mapper.EnumMapperValue;

import java.util.List;

public record EmailConvertPolicyContext(
        EnumMapperValue convertType,
        String bodyKey,
        String coverKey,
        List<ConvertedAttachment> attachments
) {

    public static EmailConvertPolicyContext of(EnumMapperValue convertType, EmailConvertResolveCommand command) {
        return new EmailConvertPolicyContext(
                convertType,
                command.bodyKey(),
                command.coverKey(),
                command.determineAttachments()
        );
    }


}
