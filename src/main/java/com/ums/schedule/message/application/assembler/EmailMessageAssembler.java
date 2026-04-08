package com.ums.schedule.message.application.assembler;

import com.ums.schedule.attachment.application.handler.EmailBodyHandler;
import com.ums.schedule.attachment.application.resolver.AttachmentCreator;
import com.ums.schedule.attachment.application.resolver.EmailBodyCreator;
import com.ums.schedule.attachment.domain.Attachment;
import com.ums.schedule.attachment.exception.AttachmentPolicyRequiredException;
import com.ums.schedule.common.code.EnumMapperFactory;
import com.ums.schedule.common.code.EnumMapperValue;
import com.ums.schedule.message.application.command.EmailMessageCommand;
import com.ums.schedule.message.domain.SendMessage;
import com.ums.schedule.message.domain.email.EmailSendMessage;
import com.ums.schedule.send.application.model.dto.SendTargetDto;
import com.ums.schedule.template.application.assembler.TemplateAssembler;
import com.ums.schedule.template.application.resolver.TemplateTypeResolver;
import com.ums.schedule.template.application.response.email.EmailContentResponse;
import com.ums.schedule.template.application.response.email.EmailTemplateDetailResponse;
import com.ums.schedule.template.application.response.email.EmailTemplateResponse;
import com.ums.schedule.template.domain.TemplateTypeContent;
import com.ums.schedule.template.domain.email.EmailTemplate;
import com.ums.schedule.template.infrastructure.TemplateClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static com.ums.schedule.template.domain.TemplateTypeContent.ofWithoutPrefix;
import static com.ums.schedule.template.domain.code.EmailTemplateSectionEnum.*;
import static com.ums.schedule.template.domain.code.TemplateEnumMapper.*;

@Component
@RequiredArgsConstructor
public class EmailMessageAssembler {
    private final EnumMapperFactory enumMapperFactory;
    private final TemplateAssembler assembler;
    private final Map<String, TemplateTypeResolver> resolverMap;
    private final Map<String, EmailBodyCreator> creatorMap;

    public SendMessage createMessage(EmailMessageCommand command, EmailTemplateResponse response, SendTargetDto targetDto) {
        EmailTemplateDetailResponse emailTemplate = response.emailTemplate();

        EnumMapperValue templateType = enumMapperFactory.findEnumMapperValue(TEMPLATE_TYPE, response.template().templateType());
        TemplateTypeContent titleContent = getTitle(templateType, emailTemplate.msgTitle());

        // 여기서 업로드
        EnumMapperValue section = enumMapperFactory.findEnumMapperValue(EMAIL_TEMPLATE_SECTION, emailTemplate.getBody().section());
        AttachmentCreator creator = getAttachmentCreator(section);
        Attachment fromBody = creator.createAttachment(command, emailTemplate.getBody(), targetDto);
        List<Attachment> toAttachmentList = toAttachmentList(command, targetDto, emailTemplate.getAttachmentList());
        List<Attachment> attachments = addAttachment(fromBody, toAttachmentList);

        EmailContentResponse getBody = getBody(emailTemplate, fromBody);
        EmailTemplate template = assembler.assemble(emailTemplate, getBody);

        return EmailSendMessage.of(titleContent, template, attachments);
    }

    private List<Attachment> toAttachmentList(EmailMessageCommand command, SendTargetDto targetDto, List<EmailContentResponse> attachmentList) {
        AttachmentCreator creator = getAttachmentCreator(EnumMapperValue.fromEnumMapperType(ATTACHMENT));
        return attachmentList
                .stream()
                .map(attach -> creator.createAttachment(command, attach, targetDto))
                .toList();
    }

    private AttachmentCreator getAttachmentCreator(EnumMapperValue section) {
        return creatorMap.get(section.value());
    }

    private EmailContentResponse getBody(EmailTemplateDetailResponse template, Attachment attachment) {
        if(attachment != null) {
            return Optional.ofNullable(template.getHeaderFooter().get(COVER))
                    .orElse(null);
        }
        return template.getBody();
    }

    private TemplateTypeContent getTitle(EnumMapperValue templateType, String msgTitle) {
        return Optional.ofNullable(resolverMap.get(templateType.value()))
                .map(resolver -> resolver.appendPrefixTexture(msgTitle))
                .orElse(ofWithoutPrefix(templateType, msgTitle));
    }

    private List<Attachment> addAttachment(Attachment body, List<Attachment> attachmentList) {
        return Stream.concat(
                Optional.ofNullable(body)
                        .map(Stream::of)
                        .orElseGet(Stream::empty),
                attachmentList.stream()
        ).toList();
    }
}
