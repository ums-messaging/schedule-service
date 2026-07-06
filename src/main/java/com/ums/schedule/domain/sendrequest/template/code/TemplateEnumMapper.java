package com.ums.schedule.domain.sendrequest.template.code;


import com.ums.schedule.domain.sendrequest.resource.email.code.ConvertTypeEnum;
import com.ums.schedule.domain.sendrequest.template.email.code.EmailTemplateSectionEnum;
import com.ums.schedule.domain.sendrequest.template.email.code.TemplateContentFormatEnum;
import com.ums.schedule.common.code.mapper.EnumMapper;
import com.ums.schedule.common.code.mapper.EnumMapperType;

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
