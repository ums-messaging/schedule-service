package com.ums.schedule.application.channel.email.template.loader;

import com.ums.schedule.adapter.storage.AwsS3Repository;
import com.ums.schedule.code.EnumMapperValue;
import com.ums.schedule.adapter.api.template.email.EmailContentResponse;
import com.ums.schedule.code.email.TemplateContentFormatEnum;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;


@Component
@RequiredArgsConstructor
public class TemplateFileLoader implements EmailTemplateLoader {
    private final AwsS3Repository repository;
    private final Configuration configuration;

    @Override
    public boolean supports(EnumMapperValue mapperValue) {
        return TemplateContentFormatEnum.valueOf(mapperValue.value()) == TemplateContentFormatEnum.HTML;
    }

    @Override
    public Template loadTemplate(EmailContentResponse content) throws IOException {
        InputStream inputStream = repository.getFileContent(content.fileKey());
        InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        return new Template(content.section(), reader.toString(), configuration);
    }
}
