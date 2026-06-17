package com.ums.schedule.domain.sendrequest.template.email;

import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.domain.sendrequest.template.exception.TemplateContentRequiredException;
import com.ums.schedule.domain.sendrequest.template.code.TemplateTypeEnum;
import org.springframework.util.StringUtils;

public record EmailTitle(
        String title
) {
    public static EmailTitle of(EnumMapperValue templateTypeValue, String prefix, String msgTitle) {
        TemplateTypeEnum templateType = TemplateTypeEnum.valueOf(templateTypeValue.code());
        String title = (templateType == TemplateTypeEnum.ADVERTISE) ? prefix.concat(" " + msgTitle) : msgTitle;
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
