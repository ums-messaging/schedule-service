package com.ums.schedule.application.ums.email.convert.strategy;

import com.ums.schedule.application.exception.ConvertMessageNotConfiguredException;
import com.ums.schedule.application.ums.email.config.EmailMessageProperties;
import com.ums.schedule.application.ums.email.convert.ConvertedAttachment;
import com.ums.schedule.application.ums.email.convert.strategy.model.EmailConvertResult;
import com.ums.schedule.application.ums.email.convert.strategy.model.EmailConvertPolicyContext;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.domain.message.email.SecurityMailPolicy;
import com.ums.schedule.domain.message.email.code.ConvertTypeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AttachmentEmailConvertPolicy implements EmailMessageConvertStrategy {
    private final EmailMessageProperties properties;
    @Override
    public boolean supports(EnumMapperValue mapperValue) {
        return !ConvertTypeEnum.NONE.equals(ConvertTypeEnum.valueOf(mapperValue.code()));
    }

    @Override
    public EmailConvertResult convert(EmailConvertPolicyContext context) {

        ConvertedAttachment convertedAttachment = convertToAttachment(context);
        SecurityMailPolicy securityPolicy = Optional.ofNullable(context.securityMail())
                .map(SecurityMailPolicy::of)
                .orElse(null);
        return EmailConvertResult.of(context.coverKey(), securityPolicy, convertedAttachment);
    }

    private ConvertedAttachment convertToAttachment(EmailConvertPolicyContext context) {
        String fileKeyTemplate = properties.getConvertFileKeyTemplate();
        if (!StringUtils.hasText(fileKeyTemplate)) {
            throw ConvertMessageNotConfiguredException.of("convert.file_key_template");
        }
        return ConvertedAttachment.of(context.convertType(), context.body(), fileKeyTemplate);
    }
}
