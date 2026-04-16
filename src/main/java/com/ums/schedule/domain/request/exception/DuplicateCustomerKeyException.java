package com.ums.schedule.domain.request.exception;

public class DuplicateCustomerKeyException extends SendRequestException {

    protected DuplicateCustomerKeyException(String message) {
        super(message);
    }

    public static DuplicateCustomerKeyException of() {
        return new DuplicateCustomerKeyException("CustomerRequestKey는 중복될 수 없습니다.");
    }
}
