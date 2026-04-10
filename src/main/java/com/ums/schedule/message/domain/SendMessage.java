package com.ums.schedule.message.domain;

import com.ums.schedule.common.code.EnumMapperValue;
import com.ums.schedule.message.code.ChannelTypeEnum;
import com.ums.schedule.message.code.MessageStatusEnum;
import com.ums.schedule.message.domain.status.MessageState;
import com.ums.schedule.message.domain.status.MessageWaitState;
import com.ums.schedule.send.domain.target.EmailSendTarget;
import com.ums.schedule.template.domain.email.EmailTitle;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class SendMessage {
    private ChannelTypeEnum channelType;
    private EmailTitle titleContent;
    private MessageState state;
    private MessageStatusEnum status;
    private String title;
    private String template;

    private EmailSendTarget target;

    protected SendMessage(EnumMapperValue channelType, EmailTitle titleContent, EmailSendTarget target) {
        this.titleContent = titleContent;
        this.title = titleContent.toWithPrefix();
        this.target = target;
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
