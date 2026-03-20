package com.ums.schedule.template.application.resolver;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdvertiseFormatResolver implements MessageFormatResolver {
    @Value("${template.message_type.prefix}")
    private final String TEMPLATE_MESSAGE_TYPE_PREFIX;

    @Override
    public String appendPrefixTexture(String content) {
        return this.TEMPLATE_MESSAGE_TYPE_PREFIX.concat(content);
    }
}
