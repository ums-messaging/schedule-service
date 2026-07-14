package com.ums.schedule.common.code.template;


import com.ums.schedule.common.code.message.MessageType;
import com.ums.schedule.common.code.email.ConvertTypeEnum;
import com.ums.schedule.common.code.email.EmailTemplatePathTypeEnum;
import com.ums.schedule.common.code.email.EmailTemplateSectionEnum;
import com.ums.schedule.common.code.email.TemplateContentFormatEnum;
import com.ums.schedule.common.code.mapper.EnumMapper;
import com.ums.schedule.common.code.mapper.EnumMapperType;

public enum TemplateEnumMapper implements EnumMapper {
    TEMPLATE_FORMAT(TemplateContentFormatEnum.class),
    TEMPLATE_TYPE(MessageType.class),
    EMAIL_TEMPLATE_SECTION(EmailTemplateSectionEnum.class),
    EMAIL_TEMPLATE_PATH(EmailTemplatePathTypeEnum.class),
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
