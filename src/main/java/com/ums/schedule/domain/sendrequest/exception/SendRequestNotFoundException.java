package com.ums.schedule.domain.sendrequest.exception;

import com.ums.schedule.common.exception.ResourceNotFoundException;

public class SendRequestNotFoundException extends ResourceNotFoundException {
    protected SendRequestNotFoundException() {
        super("SendRequest");
    }

    public static SendRequestNotFoundException of() {
        return new SendRequestNotFoundException();
    }
}
