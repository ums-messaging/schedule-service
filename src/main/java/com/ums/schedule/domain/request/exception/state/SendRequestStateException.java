package com.ums.schedule.domain.request.exception.state;

import com.ums.schedule.code.send.SendRequestStatusEnum;
import com.ums.schedule.domain.request.exception.SendRequestException;

public abstract class SendRequestStateException extends SendRequestException {
    protected SendRequestStateException(SendRequestStatusEnum from, SendRequestStatusEnum to) {
        super(String.format("%s -> %s 상태로 변경할 수 없습니다.", from.description(), to.description()));
    }
}
