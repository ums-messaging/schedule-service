package com.ums.schedule.application.ums.email.convert.resolver.model;

import com.ums.schedule.adapter.api.request.email.EmailSendCreateRequest;
import com.ums.schedule.application.ums.email.convert.ConvertedAttachment;
import com.ums.schedule.application.ums.email.convert.strategy.model.EmailConvertPolicyContext;
import com.ums.schedule.application.ums.email.template.query.model.EmailTemplateDetailResult;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.domain.message.email.code.AttachmentType;
import com.ums.schedule.domain.sendrequest.template.email.code.EmailTemplateSectionEnum;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

public record EmailConvertResolveCommand(
        String convertType,
        String headerKey,
        String bodyKey,
        String coverKey,
        String footerKey,
        List<AttachmentResolveCommand> attachmentList
) {

    public static EmailConvertResolveCommand of(EmailSendCreateRequest request, EmailTemplateDetailResult template) {
        return new EmailConvertResolveCommand(
                request.convertType(),
                template.getHeaderFooter().get(EmailTemplateSectionEnum.HEADER).fileKey(),
                template.getHeaderFooter().get(EmailTemplateSectionEnum.BODY).fileKey(),
                template.getHeaderFooter().get(EmailTemplateSectionEnum.COVER).fileKey(),
                template.getHeaderFooter().get(EmailTemplateSectionEnum.BODY).fileKey(),
                template.toAttachmentCommandList()
        );
    }

    public List<ConvertedAttachment> determineAttachments() {
        return attachmentList()
                .stream()
                .map(attachment -> determineAttachment(attachment))
                .toList();
    }

    private ConvertedAttachment determineAttachment(AttachmentResolveCommand command) {
        return Optional.ofNullable(command.fileKey())
                .filter(StringUtils::hasText)
                .map(key -> ConvertedAttachment.of(AttachmentType.DIRECT, key))
                .orElseGet(() -> ConvertedAttachment.of(AttachmentType.TEMPLATE, command.fileKeyTemplate()));
    }

    public EmailConvertPolicyContext toPolicyCommand(EnumMapperValue convertType) {
        return EmailConvertPolicyContext.of(convertType, this);
    }
}
