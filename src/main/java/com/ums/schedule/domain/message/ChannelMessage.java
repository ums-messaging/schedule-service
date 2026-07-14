package com.ums.schedule.domain.message;

import com.ums.schedule.common.code.common.ChannelTypeEnum;

import java.util.UUID;

public interface ChannelMessage {
    ChannelTypeEnum channelType();
    UUID getId();
}
