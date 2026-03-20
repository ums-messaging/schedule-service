package com.ums.schedule.template.application.assembler;

import com.ums.schedule.attachment.application.service.AttachmentHandler;
import com.ums.schedule.attachment.domain.Attachment;
import com.ums.schedule.common.code.EnumMapperFactory;
import com.ums.schedule.common.code.EnumMapperValue;
import com.ums.schedule.message.application.assembler.EmailMessageAssembler;
import com.ums.schedule.message.application.command.EmailMessageCommand;
import com.ums.schedule.message.domain.email.EmailSendMessage;
import com.ums.schedule.template.application.resolver.TemplateFormatResolver;
import com.ums.schedule.template.application.response.TemplateResponse;
import com.ums.schedule.template.application.response.email.EmailContentResponse;
import com.ums.schedule.template.application.response.email.EmailTemplateDetailResponse;
import com.ums.schedule.template.application.response.email.EmailTemplateResponse;
import com.ums.schedule.template.domain.code.EmailTemplateSectionEnum;
import com.ums.schedule.template.domain.code.TemplateContentFormatEnum;
import com.ums.schedule.template.domain.email.EmailContent;
import com.ums.schedule.template.domain.email.EmailTemplate;
import com.ums.schedule.template.infrastructure.TemplateClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.ums.schedule.template.domain.code.EmailTemplateSectionEnum.*;
import static com.ums.schedule.template.domain.code.TemplateEnumMapper.*;

@Component
@RequiredArgsConstructor
public class TemplateAssembler {
    private final Map<String, TemplateFormatResolver> resolverMap;
    private final EnumMapperFactory enumMapperFactory;

    public EmailTemplate assemble(EmailTemplateDetailResponse template, EmailContentResponse body) {
        EmailContentResponse header = template.getHeaderFooter().get(FOOTER.value());
        EmailContentResponse footer = template.getHeaderFooter().get(FOOTER.value());
        Map<EmailTemplateSectionEnum, EmailContent> contentMap = loadTemplate(header, body, footer);

        return EmailTemplate.of(template.emailContentId(), contentMap);
    }

    public Map<EmailTemplateSectionEnum, EmailContent> loadTemplate(EmailContentResponse header, EmailContentResponse body, EmailContentResponse footer) {
        EmailContent readHeader = resolverMap.get(resolveTemplateSectionEnum(header.section())).loadTemplate(header);
        EmailContent readBody = resolverMap.get(resolveTemplateSectionEnum(body.section())).loadTemplate(body);
        EmailContent readFooter = resolverMap.get(resolveTemplateSectionEnum(footer.section())).loadTemplate(footer);
        return Map.of(
                HEADER, readHeader,
                BODY, readBody,
                FOOTER, readFooter
        );
    }

    private EmailTemplateSectionEnum resolveTemplateSectionEnum(String section) {
        EnumMapperValue enumMapperValue = enumMapperFactory.findEnumMapperValue(EMAIL_TEMPLATE_SECTION, section);
        return EmailTemplateSectionEnum.valueOf(enumMapperValue.value());
    }

}
