package com.ums.schedule.application.ums.email.template;

import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.common.code.message.MessageType;
import com.ums.schedule.domain.exception.template.TemplateContentRequiredException;
import org.springframework.util.StringUtils;

public record EmailTitle(
        String title
) {
    public static EmailTitle of(EnumMapperValue templateTypeValue, String prefix, String msgTitle) {
        MessageType templateType = MessageType.valueOf(templateTypeValue.code());
        String title = (templateType == MessageType.ADVERTISE) ? prefix.concat(" " + msgTitle) : msgTitle;
        EmailTitle result = new EmailTitle(title);
        result.validate(msgTitle);
        return result;
    }

    private void validate(String title) {
        if(!StringUtils.hasText(title)) {
            throw TemplateContentRequiredException.ofTitle();
        }
    }
}
