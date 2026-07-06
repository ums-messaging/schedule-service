package com.ums.schedule.domain.sendrequest.message;

import com.ums.schedule.domain.sendrequest.code.ChannelTypeEnum;

import java.util.UUID;

public interface ChannelMessage {
    ChannelTypeEnum channelType();
    UUID getId();
}
