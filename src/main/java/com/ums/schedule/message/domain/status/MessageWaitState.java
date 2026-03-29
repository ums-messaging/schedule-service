package com.ums.schedule.message.domain.status;

import com.ums.schedule.message.code.MessageStatusEnum;

public class MessageWaitState implements MessageState {

    @Override
    public MessageState toActive() {
       return new MessageActiveState();
    }

    @Override
    public MessageState toInActive() {
        return new MessageInActiveState();
    }

    @Override
    public MessageStatusEnum currentState() {
        return MessageStatusEnum.WAIT;
    }
}
