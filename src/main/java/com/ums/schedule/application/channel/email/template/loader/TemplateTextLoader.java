package com.ums.schedule.application.channel.email.template.loader;

import com.ums.schedule.code.EnumMapperValue;
import com.ums.schedule.adapter.api.template.email.EmailContentResponse;
import com.ums.schedule.code.email.TemplateContentFormatEnum;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class TemplateTextLoader implements EmailTemplateLoader {
    private final Configuration configuration;
    @Override
    public boolean supports(EnumMapperValue mapperValue) {
        return TemplateContentFormatEnum.valueOf(mapperValue.value()) == TemplateContentFormatEnum.TEXT;
    }

    @Override
    public Template loadTemplate(EmailContentResponse content) throws IOException {
        return new Template(content.section(), content.content(), configuration);
    }
}
