package com.ums.schedule.message.domain.status;

import com.ums.schedule.message.code.MessageStatusEnum;

public class MessageInActiveState implements MessageState {
    @Override
    public MessageStatusEnum currentState() {
        return MessageStatusEnum.INACTIVE;
    }
}
