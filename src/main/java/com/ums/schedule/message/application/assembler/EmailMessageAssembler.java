package com.ums.schedule.message.application.assembler;

import com.ums.schedule.attachment.application.handler.AttachmentHandler;
import com.ums.schedule.attachment.domain.Attachment;
import com.ums.schedule.common.code.EnumMapperFactory;
import com.ums.schedule.common.code.EnumMapperValue;
import com.ums.schedule.message.application.command.EmailMessageCommand;
import com.ums.schedule.message.domain.SendMessage;
import com.ums.schedule.message.domain.email.EmailSendMessage;
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

import static com.ums.schedule.template.domain.TemplateTypeContent.ofWithoutPrefix;
import static com.ums.schedule.template.domain.code.EmailTemplateSectionEnum.*;
import static com.ums.schedule.template.domain.code.TemplateEnumMapper.*;

@Component
@RequiredArgsConstructor
public class EmailMessageAssembler {
    private final TemplateClient templateClient;
    private final EnumMapperFactory enumMapperFactory;
    private final AttachmentHandler handler;
    private final TemplateAssembler assembler;
    private final Map<String, TemplateTypeResolver> resolverMap;

    public SendMessage createMessage(EmailMessageCommand command) {
        EmailTemplateResponse response = templateClient.getTemplate(command.templateKey());
        EmailTemplateDetailResponse emailTemplate = response.emailTemplate();

        EnumMapperValue templateType = enumMapperFactory.findEnumMapperValue(TEMPLATE_TYPE, response.template().templateType());
        TemplateTypeContent titleContent = getTitle(templateType, emailTemplate.msgTitle());

        Attachment bodyToAttachment = handler.handle(command, response.emailTemplate().getBody());
        EmailContentResponse getBody = getBody(emailTemplate, bodyToAttachment);
        EmailTemplate template = assembler.assemble(emailTemplate, getBody);

        List<Attachment> attachmentList = handler.handle(command, bodyToAttachment, emailTemplate.getAttachmentList());
        return EmailSendMessage.of(titleContent, template, attachmentList);
    }

    private EmailContentResponse getBody(EmailTemplateDetailResponse template, Attachment bodyToAttachment) {
        if(bodyToAttachment != null) {
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
}
