package com.ums.schedule.code.email;


import com.ums.schedule.code.template.TemplateTypeEnum;
import com.ums.schedule.code.EnumMapper;
import com.ums.schedule.code.EnumMapperType;

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
        return this.name();
    }

    @Override
    public Class<? extends EnumMapperType> code() {
        return this.code;
    }
}
