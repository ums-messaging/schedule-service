package com.ums.schedule.message.domain;

import com.ums.schedule.common.code.EnumMapperValue;
import com.ums.schedule.message.code.ChannelTypeEnum;
import com.ums.schedule.template.domain.code.TemplateTypeEnum;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public abstract class SendMessage {
    private ChannelTypeEnum channelType;
    private TemplateTypeEnum templateType;
    private String title;
    private String template;

    protected SendMessage(EnumMapperValue channelType, EnumMapperValue templateType, String title) {
        resolveChannelTypeEnum(channelType);
        resolveTemplateTypeEnum(templateType);
        this.title = title;
    }

    private void resolveChannelTypeEnum(EnumMapperValue channelType) {
        this.channelType = ChannelTypeEnum.valueOf(channelType.value());
    }

    private void resolveTemplateTypeEnum(EnumMapperValue templateType) {
        this.templateType = TemplateTypeEnum.valueOf(templateType.value());
    }

    protected void writeTemplate(String template) {
        this.template = template;
    }
}
