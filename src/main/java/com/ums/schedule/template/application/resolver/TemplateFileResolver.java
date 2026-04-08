package com.ums.schedule.template.application.resolver;

import com.ums.schedule.attachment.infrastructure.AwsS3Repository;
import com.ums.schedule.common.code.EnumMapperValue;
import com.ums.schedule.template.application.response.email.EmailContentResponse;
import com.ums.schedule.template.domain.code.TemplateContentFormatEnum;
import com.ums.schedule.template.domain.email.EmailContent;
import com.ums.schedule.template.domain.email.EmailTemplate;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.ums.schedule.common.code.EnumMapperValue.fromEnumMapperType;
import static com.ums.schedule.template.domain.code.TemplateContentFormatEnum.HTML;
import static com.ums.schedule.template.domain.code.TemplateContentFormatEnum.valueOf;

@Component
@RequiredArgsConstructor
public class TemplateFileResolver implements TemplateFormatResolver {
    private final AwsS3Repository repository;
    private final Configuration configuration;

    @Override
    public boolean supports(EnumMapperValue mapperValue) {
        return valueOf(mapperValue.value()) == HTML;
    }


    @Override
    public Template loadTemplate(EmailContentResponse content) throws IOException {
        String template = repository.getFileContent(content.fileKey());
        return new Template(content.section(), template, configuration);
    }
}
