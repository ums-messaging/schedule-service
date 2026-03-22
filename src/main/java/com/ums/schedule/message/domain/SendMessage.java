package com.ums.schedule.message.domain;

import com.ums.schedule.common.code.EnumMapperValue;
import com.ums.schedule.message.code.ChannelTypeEnum;
import com.ums.schedule.template.domain.TemplateTypeContent;
import com.ums.schedule.template.domain.code.TemplateTypeEnum;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public abstract class SendMessage {
    private ChannelTypeEnum channelType;
    private TemplateTypeContent titleContent;
    private String title;
    private String template;

    protected SendMessage(EnumMapperValue channelType, TemplateTypeContent titleContent) {
        resolveChannelTypeEnum(channelType);
        this.titleContent = titleContent;
        this.title = titleContent.toWithPrefix();
    }

    private void resolveChannelTypeEnum(EnumMapperValue channelType) {
        this.channelType = ChannelTypeEnum.valueOf(channelType.value());
    }

    protected void writeTemplate(String template) {
        this.template = template;
    }
}
