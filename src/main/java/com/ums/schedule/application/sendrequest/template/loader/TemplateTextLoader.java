package com.ums.schedule.application.sendrequest.template.loader;

import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.domain.sendrequest.template.email.code.TemplateContentFormatEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TemplateTextLoader implements TemplateLoader {
    @Override
    public boolean supports(EnumMapperValue mapperValue) {
        return TemplateContentFormatEnum.valueOf(mapperValue.value()) == TemplateContentFormatEnum.TEXT;
    }

    @Override
    public String loadTemplate(String content)  {
        return content;
    }
}
