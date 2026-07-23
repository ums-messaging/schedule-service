package com.ums.schedule.application.ums.email.convert.strategy;

import com.ums.schedule.application.ums.email.attachment.model.AttachmentContext;
import com.ums.schedule.application.ums.email.config.EmailMessageProperties;
import com.ums.schedule.application.ums.email.convert.ConvertedAttachment;
import com.ums.schedule.application.ums.email.convert.resolver.model.EmailConvertResolveCommand;
import com.ums.schedule.application.ums.email.convert.strategy.model.EmailConvertResult;
import com.ums.schedule.application.ums.email.exception.EmailConvertTypeNotSupportedException;
import com.ums.schedule.application.ums.email.template.exception.EmailTemplateNotConfiguredException;
import com.ums.schedule.common.code.api.AttachmentErrorCode;
import com.ums.schedule.common.code.api.EmailMessageErrorCode;
import com.ums.schedule.common.code.email.ConvertType;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.common.code.email.EmailMessageSection;
import com.ums.schedule.domain.message.email.exception.AttachmentPolicyViolationException;
import com.ums.schedule.domain.message.email.exception.EmailContentMissingException;
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
        return !ConvertType.NONE.equals(ConvertType.valueOf(mapperValue.code()));
    }

    @Override
    public EmailConvertResult convert(EnumMapperValue convertType, EmailConvertResolveCommand command) {
        ConvertedAttachment convertedAttachment = convertToAttachment(convertType, command.body());
        String bodyKey = Optional.ofNullable(command.cover())
                .map(AttachmentContext::key)
                .orElseThrow(() -> EmailContentMissingException.of(EmailMessageSection.COVER));
        return EmailConvertResult.of(bodyKey, convertedAttachment);
    }

    private ConvertedAttachment convertToAttachment(EnumMapperValue convertTypeValue, AttachmentContext body) {
        ConvertType convertType = resolveConvertType(convertTypeValue);
        String fileKeyTemplate = properties.getConvertFileKeyTemplate();
        if (!StringUtils.hasText(fileKeyTemplate)) {
            throw EmailTemplateNotConfiguredException.of(EmailMessageErrorCode.NOT_CONFIGURED_FILE_KEY_TEMPLATE);
        }
        return ConvertedAttachment.of(convertType, body, fileKeyTemplate);
    }

    private ConvertType resolveConvertType(EnumMapperValue convertType) {
        return Optional.ofNullable(convertType)
                .map(v -> ConvertType.valueOf(v.code()))
                .filter(v -> v != ConvertType.NONE)
                .orElseThrow(() -> EmailConvertTypeNotSupportedException.of(ConvertType.valueOf(convertType.code())));
    }
}
