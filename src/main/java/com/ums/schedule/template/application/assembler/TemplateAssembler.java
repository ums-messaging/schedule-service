package com.ums.schedule.template.application.assembler;

import com.ums.schedule.common.code.EnumMapperFactory;
import com.ums.schedule.common.code.EnumMapperValue;
import com.ums.schedule.template.application.response.TemplateResponse;
import com.ums.schedule.template.application.response.email.EmailContentResponse;
import com.ums.schedule.template.application.response.email.EmailTemplateResponse;
import com.ums.schedule.template.domain.code.EmailTemplateSectionEnum;
import com.ums.schedule.template.domain.email.EmailContent;
import com.ums.schedule.template.domain.email.EmailTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.ums.schedule.template.domain.code.TemplateEnumMapper.*;

@Component
@RequiredArgsConstructor
public class TemplateAssembler {
    private final EnumMapperFactory enumMapperFactory;

    public EmailTemplate toTemplateMap(EmailTemplateResponse response) {
        EnumMapperValue templateType = enumMapperFactory.findEnumMapperValue(TEMPLATE_TYPE, response.template().templateType());
        Map<EmailTemplateSectionEnum, EmailContent> contentMap = toContentMap(response.emailTemplate().contents());
        EmailTemplate template = EmailTemplate.of(response, templateType, contentMap);
        return template;
    }

    private Map<EmailTemplateSectionEnum, EmailContent> toContentMap(List<EmailContentResponse> contents) {
        return contents.stream()
                .collect(Collectors.toMap(
                        content -> resolveTemplateSectionEnum(content.section()),
                        content -> {
                            EnumMapperValue formatEnum = enumMapperFactory.findEnumMapperValue(TEMPLATE_FORMAT, content.format());
                            return EmailContent.of(content, formatEnum);
                        },
                        (oldVal, newVal) -> newVal
                ));
    }

    private EmailTemplateSectionEnum resolveTemplateSectionEnum(String section) {
        EnumMapperValue enumMapperValue = enumMapperFactory.findEnumMapperValue(EMAIL_TEMPLATE_SECTION, section);
        return EmailTemplateSectionEnum.valueOf(enumMapperValue.value());
    }
}
