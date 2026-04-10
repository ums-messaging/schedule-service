package com.ums.schedule.template.application.assembler;

import com.ums.schedule.common.code.EnumMapperFactory;
import com.ums.schedule.common.code.EnumMapperValue;
import com.ums.schedule.send.domain.request.EmailBody;
import com.ums.schedule.template.application.resolver.TemplateFormatResolver;
import com.ums.schedule.template.application.response.email.EmailContentResponse;
import com.ums.schedule.template.application.response.email.EmailTemplateDetailResponse;
import com.ums.schedule.template.application.response.email.EmailTemplateResponse;
import com.ums.schedule.template.domain.email.EmailTitle;
import com.ums.schedule.template.domain.code.ConvertTypeEnum;
import com.ums.schedule.template.domain.code.EmailTemplateSectionEnum;
import com.ums.schedule.template.domain.code.TemplateContentFormatEnum;
import com.ums.schedule.template.domain.email.EmailTemplate;
import com.ums.schedule.template.infrastructure.TemplateClient;
import freemarker.template.Template;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import static com.ums.schedule.template.domain.code.EmailTemplateSectionEnum.*;
import static com.ums.schedule.template.domain.code.TemplateEnumMapper.*;

@Component
@RequiredArgsConstructor
public class EmailTemplateService {
    @Value("${template.message_type.prefix}")
    private final String TEMPLATE_MESSAGE_TYPE_PREFIX;
    private final EnumMapperFactory enumMapperFactory;
    private final TemplateClient templateClient;
    private final Map<String, TemplateFormatResolver> resolverMap;

    public EmailTemplate assemble(String templateKey, EmailBody body) {
        EmailTemplateResponse response = templateClient.getTemplate(templateKey);
        Map<EmailTemplateSectionEnum, Template> templateMap = getTemplateMap(body, response.emailTemplate());
        EmailTitle title = getEmailTitle(response.template().templateType(), response.emailTemplate().msgTitle());
        return createEmailTemplate(response, templateMap, title, body);
    }

    private EmailTemplate createEmailTemplate(EmailTemplateResponse template, Map<EmailTemplateSectionEnum, Template> templateMap, EmailTitle title, EmailBody body) {
        EmailTemplate emailTemplate = EmailTemplate.of(template.emailTemplate().emailContentId(), templateMap);
        emailTemplate.defineImageDir(template.emailTemplate().imageDir());
        emailTemplate.defineTitle(title);
        emailTemplate.defineAttachment(body, template.emailTemplate());
        return emailTemplate;
    }

    private Map<EmailTemplateSectionEnum, Template> getTemplateMap(EmailBody body, EmailTemplateDetailResponse template) {
        EmailContentResponse header = template.getHeaderFooter().get(HEADER);
        EmailContentResponse footer = template.getHeaderFooter().get(FOOTER);
        EmailContentResponse getBody = getBody(body, template);
        return loadTemplate(header, getBody, footer);
    }

    private EmailTitle getEmailTitle(String templateType, String title) {
        EnumMapperValue templateTypeValue = enumMapperFactory.findEnumMapperValue(TEMPLATE_TYPE, templateType);
        return EmailTitle.of(templateTypeValue, TEMPLATE_MESSAGE_TYPE_PREFIX, title);
    }

    private EmailContentResponse getBody(EmailBody body, EmailTemplateDetailResponse template) {
        if(body.getConvertType() != ConvertTypeEnum.NONE) {
            body.writeTemplate(readContent(template.getBody()));
            return template.getHeaderFooter().get(COVER);
        }
        return template.getBody();
    }

    public Map<EmailTemplateSectionEnum, Template> loadTemplate(EmailContentResponse header, EmailContentResponse body, EmailContentResponse footer) {
        Template readHeader = readContent(header);
        Template readBody = readContent(body);
        Template readFooter = readContent(footer);

        return Map.of(
                HEADER, readHeader,
                BODY, readBody,
                FOOTER, readFooter
        );
    }

    private Template readContent(EmailContentResponse content) {
        return Optional.ofNullable(content)
                .map(c -> resolveTemplateFormatEnum(content.format()))
                .map(format -> resolverMap.get(format.value()))
                .map(resolver -> {
                    try {
                        return resolver.loadTemplate(content);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .orElse(null);
    }

    private TemplateContentFormatEnum resolveTemplateFormatEnum(String format) {
        EnumMapperValue enumMapperValue = enumMapperFactory.findEnumMapperValue(TEMPLATE_FORMAT, format);
        return TemplateContentFormatEnum.valueOf(enumMapperValue.value());
    }
}
