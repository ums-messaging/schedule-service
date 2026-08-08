package com.ums.schedule.application.ums.email.generator.policy;

import com.ums.schedule.adapter.storage.AwsS3Repository;
import com.ums.schedule.application.sendrequest.target.data.TargetMessageData;
import com.ums.schedule.application.ums.email.exception.EmailConvertTypeNotSupportedException;
import com.ums.schedule.application.ums.email.generator.model.RenderedTemplateContent;
import com.ums.schedule.application.ums.email.generator.model.RenderedTemplate;
import com.ums.schedule.application.ums.email.generator.policy.model.EmailConvertPolicy;
import com.ums.schedule.application.ums.email.generator.handler.EmailConvertHandler;
import com.ums.schedule.application.ums.email.generator.handler.model.EmailConvertContext;
import com.ums.schedule.application.ums.email.exception.EmailMessageConvertException;
import com.ums.schedule.common.code.api.EmailMessageErrorCode;
import com.ums.schedule.common.code.email.ConvertType;
import com.ums.schedule.common.code.email.EmailMessageSection;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.domain.message.email.exception.EmailContentMissingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class AttachmentEmailConvertPolicy implements EmailMessageConvertPolicy {
    private final List<EmailConvertHandler> handlers;
    private final AwsS3Repository fileRepository;

    @Override
    public boolean supports(EnumMapperValue convertType) {
        return ConvertType.NONE != ConvertType.valueOf(convertType.code());
    }

    @Override
    public EmailConvertPolicy convert(RenderedTemplate context, TargetMessageData targetData) {
        RenderedTemplateContent body = Optional.ofNullable(context.body()).orElseThrow(() -> EmailContentMissingException.of(EmailMessageSection.BODY));
        RenderedTemplateContent cover = Optional.ofNullable(context.cover()).orElseThrow(() -> EmailContentMissingException.of(EmailMessageSection.COVER));

        File file = handlers.stream()
                .filter(h -> h.supports(context.convertType(), context.emailType()))
                .map(h -> executeConvertHandler(context, targetData, h))
                .findFirst()
                .orElseThrow(() -> EmailMessageConvertException.of(EmailMessageErrorCode.NOT_CONVERT_MESSAGE));

        fileRepository.upload(file, body.fileKey());

        List<RenderedTemplateContent> attachments =
                combineAttachmentList(context.body(), context.attachments());

        return EmailConvertPolicy.of(context.convertType(), cover.template(), attachments);
    }

    private List<RenderedTemplateContent> combineAttachmentList(RenderedTemplateContent body, List<RenderedTemplateContent> attachments) {
        return Stream.concat(
                    Stream.ofNullable(body),
                    attachments.stream()
                )
                .toList();
    }

    private File executeConvertHandler(RenderedTemplate template, TargetMessageData targetData, EmailConvertHandler h) {
        try {
            EmailConvertContext context = EmailConvertContext.of(template, targetData);
            return h.handle(context);
        } catch (IOException e) {
            throw EmailMessageConvertException.of(e);
        }
    }
}