package com.ums.schedule.send.domain.request.status.exception;

import com.ums.schedule.send.domain.exception.SendRequestException;

public abstract class SendStatusException extends SendRequestException {
    public SendStatusException(String message) {
        super(message);
    }

}
