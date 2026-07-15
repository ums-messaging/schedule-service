package com.ums.schedule.domain.exception.request;

import com.ums.schedule.domain.exception.ResourceNotFoundException;

public class SendMessageNotFoundException extends ResourceNotFoundException {
    protected SendMessageNotFoundException(String resource) {
        super(resource);
    }

    public static SendMessageNotFoundException of() {
        return new SendMessageNotFoundException("send message");
    }
}
