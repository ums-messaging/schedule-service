package com.ums.schedule.domain.request.exception.customer_key;

import com.ums.schedule.domain.request.exception.SendRequestException;

public class CustomerKeyDuplicatedException extends SendRequestException {

    protected CustomerKeyDuplicatedException(String message) {
        super(message);
    }

    public static CustomerKeyDuplicatedException of() {
        return new CustomerKeyDuplicatedException("CustomerRequestKey는 중복될 수 없습니다.");
    }
}
