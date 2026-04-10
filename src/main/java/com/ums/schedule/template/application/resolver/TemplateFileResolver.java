package com.ums.schedule.template.application.resolver;

import com.ums.schedule.attachment.infrastructure.AwsS3Repository;
import com.ums.schedule.common.code.EnumMapperValue;
import com.ums.schedule.template.application.response.email.EmailContentResponse;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

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
        InputStream inputStream = repository.getFileContent(content.fileKey());
        InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        return new Template(content.section(), reader.toString(), configuration);
    }
}
