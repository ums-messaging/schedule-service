package com.ums.schedule.application.channel.email.template;

import com.ums.schedule.application.channel.email.template.loader.EmailTemplateLoader;
import com.ums.schedule.code.email.EmailTemplateSectionEnum;
import com.ums.schedule.code.email.TemplateContentFormatEnum;
import com.ums.schedule.code.email.TemplateEnumMapper;
import com.ums.schedule.code.EnumMapperFactory;
import com.ums.schedule.code.EnumMapperValue;
import com.ums.schedule.domain.channel.email.message.EmailTemplate;
import com.ums.schedule.adapter.api.template.email.EmailTemplateClient;
import com.ums.schedule.domain.channel.email.message.EmailBody;
import com.ums.schedule.adapter.api.template.email.EmailContentResponse;
import com.ums.schedule.adapter.api.template.email.EmailTemplateDetailResponse;
import com.ums.schedule.adapter.api.template.email.EmailTemplateResponse;
import com.ums.schedule.domain.channel.email.message.EmailTitle;
import com.ums.schedule.application.template.TemplateService;
import freemarker.template.Template;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class EmailTemplateService implements TemplateService {
    @Value("${template.message_type.prefix}")
    private final String TEMPLATE_MESSAGE_TYPE_PREFIX;
    private final EnumMapperFactory enumMapperFactory;
    private final EmailTemplateClient templateClient;
    private final Map<String, EmailTemplateLoader> resolverMap;

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
        EmailContentResponse header = template.getHeaderFooter().get(EmailTemplateSectionEnum.HEADER);
        EmailContentResponse footer = template.getHeaderFooter().get(EmailTemplateSectionEnum.FOOTER);
        EmailContentResponse getBody = getBody(body, template);
        return loadTemplate(header, getBody, footer);
    }

    private EmailTitle getEmailTitle(String templateType, String title) {
        EnumMapperValue templateTypeValue = enumMapperFactory.findEnumMapperValue(TemplateEnumMapper.TEMPLATE_TYPE, templateType);
        return EmailTitle.of(templateTypeValue, TEMPLATE_MESSAGE_TYPE_PREFIX, title);
    }

    private EmailContentResponse getBody(EmailBody body, EmailTemplateDetailResponse template) {
        if(body.shouldConvert()) {
            body.writeTemplate(readContent(template.getBody()));
            return template.getHeaderFooter().get(EmailTemplateSectionEnum.COVER);
        }
        return template.getBody();
    }

    public Map<EmailTemplateSectionEnum, Template> loadTemplate(EmailContentResponse header, EmailContentResponse body, EmailContentResponse footer) {
        Template readHeader = readContent(header);
        Template readBody = readContent(body);
        Template readFooter = readContent(footer);

        return Map.of(
                EmailTemplateSectionEnum.HEADER, readHeader,
                EmailTemplateSectionEnum.BODY, readBody,
                EmailTemplateSectionEnum.FOOTER, readFooter
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
        EnumMapperValue enumMapperValue = enumMapperFactory.findEnumMapperValue(TemplateEnumMapper.TEMPLATE_FORMAT, format);
        return TemplateContentFormatEnum.valueOf(enumMapperValue.value());
    }
}
