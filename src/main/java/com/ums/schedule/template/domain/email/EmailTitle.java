package com.ums.schedule.template.domain.email;

import com.ums.schedule.common.code.EnumMapperValue;
import com.ums.schedule.template.domain.code.TemplateTypeEnum;
import com.ums.schedule.template.exception.TemplateContentRequiredException;
import org.springframework.util.StringUtils;

import static com.ums.schedule.template.domain.code.TemplateTypeEnum.valueOf;

public record EmailTitle(
        String title
) {

    public static EmailTitle of(EnumMapperValue templateTypeValue, String prefix, String msgTitle) {
        TemplateTypeEnum templateType = valueOf(templateTypeValue.code());
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
