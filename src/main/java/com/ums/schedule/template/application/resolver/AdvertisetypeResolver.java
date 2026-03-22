package com.ums.schedule.template.application.resolver;


import com.ums.schedule.common.code.EnumMapperValue;
import com.ums.schedule.template.domain.TemplateTypeContent;
import com.ums.schedule.template.domain.code.TemplateTypeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static com.ums.schedule.common.code.EnumMapperValue.fromEnumMapperType;
import static com.ums.schedule.template.domain.TemplateTypeContent.ofWithPrefix;
import static com.ums.schedule.template.domain.code.TemplateTypeEnum.ADVERTISE;

@Component
@RequiredArgsConstructor
public class AdvertisetypeResolver implements TemplateTypeResolver {
    @Value("${template.message_type.prefix}")
    private final String TEMPLATE_MESSAGE_TYPE_PREFIX;

    @Override
    public TemplateTypeContent appendPrefixTexture(String content) {
        return ofWithPrefix(fromEnumMapperType(ADVERTISE), TEMPLATE_MESSAGE_TYPE_PREFIX, content);
    }
}
