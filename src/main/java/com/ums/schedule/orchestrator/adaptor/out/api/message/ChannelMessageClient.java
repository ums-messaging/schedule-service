package com.ums.schedule.orchestrator.adaptor.out.api.message;

import com.ums.schedule.common.code.mapper.EnumMapperSelector;
import com.ums.schedule.orchestrator.adaptor.out.api.message.request.ChannelSendRequest;

public interface ChannelMessageClient extends EnumMapperSelector {
    Long createChannelRequest(ChannelSendRequest sendRequest);
}
