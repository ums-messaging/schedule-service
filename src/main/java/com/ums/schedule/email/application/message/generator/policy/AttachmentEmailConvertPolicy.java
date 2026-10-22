package com.ums.schedule.email.application.message.generator.policy;

import com.ums.schedule.email.application.message.generator.policy.model.AttachMessage;
import com.ums.schedule.email.application.message.generator.resolver.ConvertMessageCommand;
import com.ums.schedule.email.domain.common.MultiPartFile;
import com.ums.schedule.email.domain.message.ConvertMessage;
import com.ums.schedule.email.domain.template.EmailTemplate;
import com.ums.schedule.email.application.message.generator.policy.model.EmailConvertPolicy;
import com.ums.schedule.common.code.email.ConvertType;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AttachmentEmailConvertPolicy implements EmailMessageConvertPolicy {

    @Override
    public boolean supports(EnumMapperValue convertType) {
        return ConvertType.NONE != ConvertType.valueOf(convertType.code());
    }

    @Override
    public EmailConvertPolicy convert(ConvertMessageCommand command) {
        EmailTemplate template = command.template();
        ConvertType convertType = ConvertType.valueOf(command.convertType().code());
        ConvertMessage convertMessage = command.convertMessage();
        AttachMessage attachMessage =
                AttachMessage.of(command.messageId(), command.securityMail(), convertMessage, template.getBody());
        return EmailConvertPolicy.of(template.getCover(), attachMessage);
    }
}