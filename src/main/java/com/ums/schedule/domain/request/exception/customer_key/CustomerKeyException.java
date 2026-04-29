package com.ums.schedule.domain.request.exception.customer_key;

import com.ums.schedule.domain.request.exception.SendRequestException;

public abstract class CustomerKeyException extends SendRequestException {
    protected CustomerKeyException(String message) {
        super(message);
    }
}
