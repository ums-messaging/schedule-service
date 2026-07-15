package com.ums.schedule.application.sendrequest.context;

import com.ums.schedule.application.sendrequest.command.SendRequestCreateCommand;
import com.ums.schedule.common.code.common.ChannelType;
import com.ums.schedule.domain.schedule.Schedule;
import com.ums.schedule.domain.request.customer.CustomerRequestKey;
import com.ums.schedule.domain.message.SendMessage;

public record SendRequestCreateContext(
        ChannelType channelType,
        Schedule schedule,
        CustomerRequestKey customerKey,
        SendMessage sendMessage,
        String templateKey,
        String senderKey,
        Integer retryCnt
) {
    public static SendRequestCreateContext of(Schedule schedule,
                                              CustomerRequestKey customerRequestKey, SendMessage message, Integer retryCount, SendRequestCreateCommand command) {
        return new SendRequestCreateContext(
                command.channelType(),
                schedule,
                customerRequestKey,
                message,
                command.templateKey(),
                command.senderKey(),
                retryCount
        );
    }


}
