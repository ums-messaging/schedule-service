package com.ums.schedule.message.domain;

import com.ums.schedule.common.code.EnumMapperValue;
import com.ums.schedule.message.code.ChannelTypeEnum;
import com.ums.schedule.template.domain.TemplateTypeContent;
import com.ums.schedule.send.domain.request.SendRequest;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class SendMessage {
    private ChannelTypeEnum channelType;
    private TemplateTypeContent titleContent;
    private String status;
    private String title;
    private String template;
    private List<SendRequest> sendRequest = new ArrayList<>();

    protected SendMessage(EnumMapperValue channelType, TemplateTypeContent titleContent) {
        resolveChannelTypeEnum(channelType);
        this.titleContent = titleContent;
        this.title = titleContent.toWithPrefix();
        this.status = "ACTIVE";
    }
    private void resolveChannelTypeEnum(EnumMapperValue channelType) {
        this.channelType = ChannelTypeEnum.valueOf(channelType.value());
    }
    protected void writeTemplate(String template) {
        this.template = template;
    }
}
