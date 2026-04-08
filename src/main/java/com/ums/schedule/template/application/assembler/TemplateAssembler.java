package com.ums.schedule.template.application.assembler;

import com.ums.schedule.common.code.EnumMapperFactory;
import com.ums.schedule.common.code.EnumMapperValue;
import com.ums.schedule.template.application.dto.EmailContentDto;
import com.ums.schedule.template.application.resolver.TemplateFormatResolver;
import com.ums.schedule.template.application.response.email.EmailContentResponse;
import com.ums.schedule.template.application.response.email.EmailTemplateDetailResponse;
import com.ums.schedule.template.domain.code.EmailTemplateSectionEnum;
import com.ums.schedule.template.domain.code.TemplateContentFormatEnum;
import com.ums.schedule.template.domain.email.EmailContent;
import com.ums.schedule.template.domain.email.EmailTemplate;
import freemarker.template.Template;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.ums.schedule.template.domain.code.EmailTemplateSectionEnum.*;
import static com.ums.schedule.template.domain.code.TemplateEnumMapper.*;

@Component
@RequiredArgsConstructor
public class TemplateAssembler {
    private final Map<String, TemplateFormatResolver> resolverMap;
    private final EnumMapperFactory enumMapperFactory;

    public EmailTemplate assemble(EmailTemplateDetailResponse template, EmailContentResponse body) {
        EmailContentResponse header = template.getHeaderFooter().get(HEADER);
        EmailContentResponse footer = template.getHeaderFooter().get(FOOTER);
        Map<EmailTemplateSectionEnum, Template> templateMap = loadTemplate(header, body, footer);
        EmailTemplate emailTemplate = EmailTemplate.of(template.emailContentId(), templateMap);
        emailTemplate.defineImageDir(template.imageDir());
        return emailTemplate;
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
