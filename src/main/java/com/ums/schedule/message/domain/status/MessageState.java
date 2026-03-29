package com.ums.schedule.message.domain.status;

import com.ums.schedule.message.code.MessageStatusEnum;
import com.ums.schedule.message.exception.MessageActiveStateException;
import com.ums.schedule.message.exception.MessageInActiveStateException;

public interface MessageState {

    default MessageState toActive() {
        throw MessageActiveStateException.of(currentState().code());
    }

    default MessageState toInActive() {
        throw MessageInActiveStateException.of(currentState().code());
    }

    MessageStatusEnum currentState();
}
