package com.ums.schedule.template.application.resolver;

import com.ums.schedule.common.code.EnumMapperValue;
import com.ums.schedule.template.application.response.email.EmailContentResponse;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.ums.schedule.template.domain.code.TemplateContentFormatEnum.*;

@Component
@RequiredArgsConstructor
public class TemplateTextResolver implements TemplateFormatResolver {
    private final Configuration configuration;
    @Override
    public boolean supports(EnumMapperValue mapperValue) {
        return valueOf(mapperValue.value()) == TEXT;
    }

    @Override
    public Template loadTemplate(EmailContentResponse content) throws IOException {
        return new Template(content.section(), content.content(), configuration);
    }
}
