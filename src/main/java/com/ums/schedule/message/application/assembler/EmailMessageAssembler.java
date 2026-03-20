package com.ums.schedule.message.application.assembler;

import com.ums.schedule.attachment.application.service.AttachmentHandler;
import com.ums.schedule.attachment.code.AttachmentEnumMapper;
import com.ums.schedule.attachment.code.StorageTypeEnum;
import com.ums.schedule.attachment.domain.Attachment;
import com.ums.schedule.attachment.domain.AttachmentPolicy;
import com.ums.schedule.attachment.domain.FileMetaData;
import com.ums.schedule.common.code.EnumMapperFactory;
import com.ums.schedule.common.code.EnumMapperValue;
import com.ums.schedule.message.application.command.EmailMessageCommand;
import com.ums.schedule.message.domain.SendMessage;
import com.ums.schedule.message.domain.email.EmailSendMessage;
import com.ums.schedule.template.application.assembler.TemplateAssembler;
import com.ums.schedule.template.application.resolver.MessageFormatResolver;
import com.ums.schedule.template.application.resolver.TemplateFormatResolver;
import com.ums.schedule.template.application.response.TemplateResponse;
import com.ums.schedule.template.application.response.email.EmailContentResponse;
import com.ums.schedule.template.application.response.email.EmailTemplateResponse;
import com.ums.schedule.template.domain.code.ConvertTypeEnum;
import com.ums.schedule.template.domain.code.EmailTemplateSectionEnum;
import com.ums.schedule.template.domain.email.EmailContent;
import com.ums.schedule.template.domain.email.EmailTemplate;
import com.ums.schedule.template.infrastructure.TemplateClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.ums.schedule.common.code.EnumMapperValue.fromEnumMapperType;
import static com.ums.schedule.template.domain.code.EmailTemplateSectionEnum.*;
import static com.ums.schedule.template.domain.code.EmailTemplateSectionEnum.FOOTER;
import static com.ums.schedule.template.domain.code.TemplateEnumMapper.*;

@Component
@RequiredArgsConstructor
public class EmailMessageAssembler {
    private final TemplateClient templateClient;
    private final EnumMapperFactory enumMapperFactory;
    private final AttachmentHandler handler;
    private final TemplateAssembler assembler;
    private final Map<String, MessageFormatResolver> messageFormatResolver;

    public SendMessage createMessage(EmailMessageCommand command) {
        EmailTemplateResponse response = templateClient.getTemplate(command.templateKey());

        EnumMapperValue templateType = enumMapperFactory.findEnumMapperValue(TEMPLATE_TYPE, response.template().templateType());
        String title = getTitle(templateType, response.emailTemplate().msgTitle());
        List<Attachment> attachmentList = handler.handler(command, response.emailTemplate().getBody(), response.emailTemplate().getAttachmentList());
        EmailTemplate template = assembler.assemble(response.emailTemplate(), getBody(response, attachmentList));

        return EmailSendMessage.of(templateType, title, template, attachmentList);
    }

    private String getTitle(EnumMapperValue templateType, String msgTitle) {

        return Optional.ofNullable(messageFormatResolver.get(templateType.value()))
                .map(resolver -> resolver.appendPrefixTexture(msgTitle))
                .orElse(msgTitle);
    }

    private EmailContentResponse getBody(EmailTemplateResponse response, List<Attachment> attachmentList) {
        return isSameAttachmentSize(response.emailTemplate().getAttachmentList(), attachmentList) ?
                response.emailTemplate().getBody() : response.emailTemplate().getHeaderFooter().get(COVER);
    }

    private boolean isSameAttachmentSize(List<EmailContentResponse> attachments, List<Attachment> toAttachments) {
        return attachments.size()+1 == toAttachments.size();
    }
}
