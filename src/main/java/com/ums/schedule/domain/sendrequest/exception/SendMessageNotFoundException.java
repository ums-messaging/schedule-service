package com.ums.schedule.domain.sendrequest.exception;

import com.ums.schedule.common.exception.ResourceNotFoundException;

public class SendMessageNotFoundException extends ResourceNotFoundException {
    protected SendMessageNotFoundException(String resource) {
        super(resource);
    }

    public static SendMessageNotFoundException of() {
        return new SendMessageNotFoundException("send message");
    }
}
