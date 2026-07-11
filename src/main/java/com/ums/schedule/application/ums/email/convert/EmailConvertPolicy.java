package com.ums.schedule.application.ums.email.convert;

import com.ums.schedule.application.ums.email.convert.resolver.model.EmailConvertResolveCommand;
import com.ums.schedule.application.ums.email.convert.strategy.model.EmailConvertResult;
import com.ums.schedule.common.code.mapper.EnumMapperValue;

import java.util.List;

public record EmailConvertPolicy(
        EnumMapperValue convertType,
        String headerKey,
        String bodyKey,
        String footerKey,
        List<ConvertedAttachment> attachmentList
) {
    public static EmailConvertPolicy of(EnumMapperValue convertType, EmailConvertResolveCommand command, EmailConvertResult context) {
        return new EmailConvertPolicy(
                convertType,
                command.headerKey(),
                context.bodyKey(),
                command.footerKey(),
                context.convertedAttachments()
        );
    }
}
