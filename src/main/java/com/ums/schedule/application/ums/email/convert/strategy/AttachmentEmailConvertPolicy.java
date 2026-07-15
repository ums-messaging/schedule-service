package com.ums.schedule.application.ums.email.convert.strategy;

import com.ums.schedule.application.exception.email.ConvertMessageNotConfiguredException;
import com.ums.schedule.application.exception.template.TemplateNotFoundException;
import com.ums.schedule.application.ums.email.attachment.model.AttachmentContext;
import com.ums.schedule.application.ums.email.config.EmailMessageProperties;
import com.ums.schedule.application.ums.email.convert.ConvertedAttachment;
import com.ums.schedule.application.ums.email.convert.resolver.model.EmailConvertResolveCommand;
import com.ums.schedule.application.ums.email.convert.strategy.model.EmailConvertResult;
import com.ums.schedule.common.code.email.ConvertType;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.application.exception.email.ConvertTypeNotSupportedException;
import com.ums.schedule.common.code.email.EmailMessageSection;
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
                .orElseThrow(() -> TemplateNotFoundException.of(EmailMessageSection.COVER));
        return EmailConvertResult.of(bodyKey, convertedAttachment);
    }

    private ConvertedAttachment convertToAttachment(EnumMapperValue convertTypeValue, AttachmentContext body) {
        ConvertType convertType = resolveConvertType(convertTypeValue);
        String fileKeyTemplate = properties.getConvertFileKeyTemplate();
        if (!StringUtils.hasText(fileKeyTemplate)) {
            throw ConvertMessageNotConfiguredException.of("convert.file_key_template");
        }
        return ConvertedAttachment.of(convertType, body, fileKeyTemplate);
    }

    private ConvertType resolveConvertType(EnumMapperValue convertTypeValue) {
        return Optional.ofNullable(convertTypeValue)
                .map(v -> ConvertType.valueOf(v.code()))
                .filter(v -> v != ConvertType.NONE)
                .orElseThrow(ConvertTypeNotSupportedException::of);
    }
}
