package com.ums.schedule.template.domain.code;

import com.ums.schedule.common.code.EnumMapper;
import com.ums.schedule.common.code.EnumMapperType;

public enum TemplateEnumMapper implements EnumMapper {

    TEMPLATE_FORMAT(TemplateContentFormatEnum.class),
    TEMPLATE_TYPE(TemplateTypeEnum.class),
    EMAIL_TEMPLATE_SECTION(EmailTemplateSectionEnum.class),
    CONVERT_TYPE(ConvertTypeEnum.class)
    ;

    Class<? extends EnumMapperType> code;

    TemplateEnumMapper(Class<? extends EnumMapperType> code) {
        this.code = code;
    }

    @Override
    public String key() {
        return null;
    }

    @Override
    public Class<? extends EnumMapperType> code() {
        return null;
    }
}
