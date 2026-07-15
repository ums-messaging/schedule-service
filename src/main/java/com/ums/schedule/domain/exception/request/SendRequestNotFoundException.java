package com.ums.schedule.domain.exception.request;

import com.ums.schedule.domain.exception.ResourceNotFoundException;

public class SendRequestNotFoundException extends ResourceNotFoundException {
    protected SendRequestNotFoundException() {
        super("SendRequest");
    }

    public static SendRequestNotFoundException of() {
        return new SendRequestNotFoundException();
    }
}
