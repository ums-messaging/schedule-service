package com.ums.schedule.email.application.message.generator.resolver;

import com.github.f4b6a3.uuid.UuidCreator;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.email.domain.common.MultiPartFile;
import com.ums.schedule.email.domain.message.ConvertMessage;
import com.ums.schedule.email.domain.message.EmailSendMessage;
import com.ums.schedule.email.domain.message.security.SecurityMail;
import com.ums.schedule.email.domain.template.EmailTemplate;

public record ConvertMessageCommand(
    String messageId,
    EnumMapperValue convertType,
    ConvertMessage convertMessage,
    SecurityMail securityMail,
    EmailTemplate template
) {
    public static ConvertMessageCommand of(EnumMapperValue convertType, EmailSendMessage message, EmailTemplate template) {
        return new ConvertMessageCommand(
                UuidCreator.toString(message.getId()),
                convertType,
                message.getConvertMessage(),
                message.getSecurityMail(),
                template
        );
    }
}
