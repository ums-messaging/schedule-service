package com.ums.schedule.domain.message;

import com.ums.schedule.common.code.common.ChannelType;

import java.util.UUID;

public interface ChannelMessage {
    ChannelType channelType();
    UUID getId();
}
