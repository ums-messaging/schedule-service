package com.ums.schedule.orchestrator.adaptor.out.api.message.response;

import com.ums.schedule.orchestrator.application.channel.message.ChannelRequestResult;

public record ChannelMessageResponse<T extends ChannelRequestResult> (
        int code,
        T details
) {

}

