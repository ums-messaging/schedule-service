package com.ums.schedule.message.domain;

import com.ums.schedule.common.code.EnumMapperValue;
import com.ums.schedule.message.code.ChannelTypeEnum;
import com.ums.schedule.message.code.MessageStatusEnum;
import com.ums.schedule.message.domain.status.MessageActiveState;
import com.ums.schedule.message.domain.status.MessageInActiveState;
import com.ums.schedule.message.domain.status.MessageState;
import com.ums.schedule.message.domain.status.MessageWaitState;
import com.ums.schedule.send.domain.target.SendTarget;
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
    private MessageState state;
    private MessageStatusEnum status;
    private String title;
    private String template;
    private SendTarget target;

    protected SendMessage(EnumMapperValue channelType, TemplateTypeContent titleContent) {
        this.titleContent = titleContent;
        this.title = titleContent.toWithPrefix();

        resolveChannelTypeEnum(channelType);
        changeMessageStatus(new MessageWaitState());
    }

    private void resolveChannelTypeEnum(EnumMapperValue channelType) {
        this.channelType = ChannelTypeEnum.valueOf(channelType.value());
    }
    protected void writeTemplate(String template) {
        this.template = template;
    }

    public void toComplete() {
        MessageState state = this.state.toActive();
        changeMessageStatus(state);
    }

    public void toInActive() {
        MessageState state = this.state.toInActive();
        changeMessageStatus(state);
    }

    private void changeMessageStatus(MessageState state) {
        this.state = state;
    }
}
