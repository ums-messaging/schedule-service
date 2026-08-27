package com.ums.schedule.application.ums.email.generator.policy;

import com.ums.schedule.adapter.storage.AwsS3Repository;
import com.ums.schedule.application.sendrequest.target.data.TargetMessageData;
import com.ums.schedule.application.ums.common.template.loader.model.EmailTemplate;
import com.ums.schedule.application.ums.common.template.loader.model.EmailTemplateContent;
import com.ums.schedule.application.ums.email.generator.policy.model.EmailConvertPolicy;
import com.ums.schedule.application.ums.email.generator.handler.EmailConvertHandler;
import com.ums.schedule.application.ums.email.generator.handler.model.EmailConvertContext;
import com.ums.schedule.application.ums.email.exception.EmailMessageConvertException;
import com.ums.schedule.application.ums.email.template.exception.EmailTemplateNotConfiguredException;
import com.ums.schedule.common.code.api.EmailMessageErrorCode;
import com.ums.schedule.common.code.email.ConvertType;
import com.ums.schedule.common.code.email.EmailMessageSection;
import com.ums.schedule.common.code.email.EmailUploadPrefixType;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.config.properties.EmailTemplateProperties;
import com.ums.schedule.domain.message.email.exception.EmailContentMissingException;
import com.ums.schedule.domain.target.message.AttachmentPayload;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class AttachmentEmailConvertPolicy implements EmailMessageConvertPolicy {
    private final List<EmailConvertHandler> handlers;
    private final AwsS3Repository fileRepository;
    private final EmailTemplateProperties properties;

    @Override
    public boolean supports(EnumMapperValue convertType) {
        return ConvertType.NONE != ConvertType.valueOf(convertType.code());
    }

    @Override
    public EmailConvertPolicy convert(EmailTemplate template, TargetMessageData targetData) {
        Template bodyTemplate = Optional.ofNullable(template.getBody())
                .map(EmailTemplateContent::template).orElseThrow(() -> EmailContentMissingException.of(EmailMessageSection.BODY));
        Template coverTemplate = Optional.ofNullable(template.getCover())
                .map(EmailTemplateContent::template).orElseThrow(() -> EmailContentMissingException.of(EmailMessageSection.COVER));
        File file = handlers.stream()
                .filter(h -> h.supports(template.getConvertType(), template.getEmailType()))
                .map(h -> executeConvertHandler(template, bodyTemplate, targetData, h))
                .findFirst()
                .orElseThrow(() -> EmailMessageConvertException.of(EmailMessageErrorCode.NOT_CONVERT_MESSAGE));

        String fileKey = generateFileKey(template.getTemplateKey(), targetData);
        fileRepository.upload(file, fileKey);

        List<AttachmentPayload> attachments =
                combineAttachmentList(AttachmentPayload.of(
                        template.getBody(), fileKey
                ), template.toPayloads());

        return EmailConvertPolicy.of(template.getConvertType(), coverTemplate, attachments);
    }

    private String generateFileKey(String templateKey, TargetMessageData targetData) {
        String templatePrefix = getTemplatePath(EmailUploadPrefixType.TEMPLATE_PREFIX, properties.getTemplateKeyPrefix());
        String attachmentSuffix = getTemplatePath(EmailUploadPrefixType.ATTACHMENT_SUFFIX, properties.getAttachmentKeySuffix());
        return "%s/%s/%s/%s/%s.pdf".formatted(
                templatePrefix,
                templateKey,
                targetData.customerId(),
                attachmentSuffix,
                targetData.targetKey());
    }

    private String getTemplatePath(EmailUploadPrefixType type, String propsValue) {
        if(!StringUtils.hasText(propsValue)) {
            throw EmailTemplateNotConfiguredException.of(type);
        }
        return propsValue;
    }

    private List<AttachmentPayload> combineAttachmentList(AttachmentPayload body, List<AttachmentPayload> attachments) {
        return Stream.concat(
                    Stream.ofNullable(body),
                    attachments.stream()
                )
                .toList();
    }

    private File executeConvertHandler(EmailTemplate template, Template bodyTemplate, TargetMessageData targetData, EmailConvertHandler h) {
        try {
            StringWriter writer = new StringWriter();
            bodyTemplate.process(targetData, writer);
            EmailConvertContext context = EmailConvertContext.of(template, writer.toString(), targetData);
            return h.handle(context);
        } catch (IOException | TemplateException e) {
            throw EmailMessageConvertException.of(e);
        }
    }
}