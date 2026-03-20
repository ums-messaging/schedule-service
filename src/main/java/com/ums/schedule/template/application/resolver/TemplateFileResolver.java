package com.ums.schedule.template.application.resolver;

import com.ums.schedule.attachment.infrastructure.AwsS3Repository;
import com.ums.schedule.common.code.EnumMapperValue;
import com.ums.schedule.template.application.response.email.EmailContentResponse;
import com.ums.schedule.template.domain.code.TemplateContentFormatEnum;
import com.ums.schedule.template.domain.email.EmailContent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.ums.schedule.template.domain.code.TemplateContentFormatEnum.HTML;
import static com.ums.schedule.template.domain.code.TemplateContentFormatEnum.valueOf;

@Component
@RequiredArgsConstructor
public class TemplateFileResolver implements TemplateFormatResolver {
    private final AwsS3Repository repository;

    @Override
    public boolean supports(EnumMapperValue mapperValue) {
        return valueOf(mapperValue.value()) == HTML;
    }


    @Override
    public EmailContent loadTemplate(EmailContentResponse content) {
        return null;
    }
}
