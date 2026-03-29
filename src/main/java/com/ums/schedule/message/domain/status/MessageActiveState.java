package com.ums.schedule.message.domain.status;

import com.ums.schedule.message.code.MessageStatusEnum;

public class MessageActiveState implements MessageState {

    @Override
    public MessageState toInActive() {
        return new MessageInActiveState();
    }

    @Override
    public MessageStatusEnum currentState() {
        return MessageStatusEnum.ACTIVE;
    }
}
