package com.ums.schedule.domain.request.exception.status;


import com.ums.schedule.domain.request.exception.SendRequestException;

public abstract class SendStatusException extends SendRequestException {
    protected SendStatusException(String message) {
        super(message);
    }
}
