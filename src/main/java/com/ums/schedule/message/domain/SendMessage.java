package com.ums.schedule.message.domain;

import com.ums.schedule.common.code.EnumMapperValue;
import com.ums.schedule.message.code.ChannelTypeEnum;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

public abstract class SendMessage {
    private String title;
    private String template;
    private ChannelTypeEnum channelType;

    protected SendMessage(EnumMapperValue channelType) {
        resolveChannelType(channelType);
    }

    private void resolveChannelType(EnumMapperValue channelType) {
        this.channelType = ChannelTypeEnum.valueOf(channelType.value());
    }

}
